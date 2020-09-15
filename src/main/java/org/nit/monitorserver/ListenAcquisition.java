package org.nit.monitorserver;

import com.google.protobuf.InvalidProtocolBufferException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.sql.SQLClient;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.schema.StscChecker;
import org.nit.monitorserver.ICDParse.ICDParse;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.database.MysqlConnection;
import org.nit.monitorserver.proto.*;
import org.nit.monitorserver.util.IniUtil;
import org.nit.monitorserver.util.Tools;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.nit.monitorserver.constant.ResponseError.INSERT_FAILURE;
import static org.nit.monitorserver.constant.ResponseError.NEW_TARGET_ERROR;
import static org.nit.monitorserver.constant.ResponseError.QUERY_FAILURE;
import static org.nit.monitorserver.constant.ResponseError.SERVER_ERROR;

//监听下位机传来的数据
public class ListenAcquisition extends AbstractVerticle {
    protected static final Logger logger = Logger.getLogger(ListenAcquisition.class);
    private int port = IniUtil.getInstance().getDacmdPort();
    private String host = IniUtil.getInstance().getServerHostName();
    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    public ListenAcquisition() {

    }

//    public void insertMysql(String sql,JsonArray params){
//        mySQLClient.queryWithParams(sql,params,res->{
//           if(res.failed()){
//               logger.error(String.format("New analyse data insert exception: %s", Tools.getTrace(res.cause())));
//           }
//        });
//    }
    @Override
    public void start(){
        DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());//创建套接字
        socket.listen(port,host,asyncResult->{//监听
            if(asyncResult.succeeded()){
                logger.info(String.format("服务器%s开始监听端口%d......", this.host, this.port));
                socket.handler(packet->{
                    try {//通信数据与分析数据
                        UdpPacket.UDP_COMMUNCATE_PACKET udppacket=UdpPacket.UDP_COMMUNCATE_PACKET.parseFrom(packet.data().getBytes());//proto格式
                        int msgtype = udppacket.getMsgType();//消息类型，一般为5，返回6
                        long  timeStamp = udppacket.getTimeStamp();//获取数据标识符
                        String targetIp = udppacket.getIpAddr();//目标机IP地址
                        String portName = udppacket.getPortname();//portName

                        mongoClient.find("targetExecuting",new JsonObject(),rq->{//查找正在执行的任务
                           if(rq.failed()){
                               logger.error(String.format("search 正在运行中的采集任务: %s 查找失败", Tools.getTrace(rq.cause())));
                               return;
                           }else if(rq.result().size() == 0){
                               logger.info("search 正在运行中的采集任务: 没有采集任务正在执行");
                               return;
                           }
                           String taskIDExecuting = rq.result().get(0).getString("taskID");
                            switch (msgtype){
                                case 4:
//                                Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
//                                String timeStampCurrent = format.format(calendar.getTime());

                                    //数据分析结果数据库
                                    AnalysisRtn.EDT_DATA_ANALYSIS_RTN data_analysis_rtn= null;
                                    try {
                                        data_analysis_rtn = AnalysisRtn.EDT_DATA_ANALYSIS_RTN.parseFrom(udppacket.getBuffers());
                                    } catch (InvalidProtocolBufferException e) {
                                        e.printStackTrace();
                                    }
//                                String sql="INSERT INTO analyse_data VALUES=(?,?,?)";
                                    String targetGuid = data_analysis_rtn.getTargetGuid();
//                                Calendar systemTime = Calendar.getInstance();
                                    JsonObject dataanalysistn= new JsonObject().put("timeStamp",timeStamp)
                                            .put("targetGuid",data_analysis_rtn.getTargetGuid())
                                            .put("targetIP",targetIp);
                                    if(data_analysis_rtn.hasEvtMemEffectRtn()){
                                        AnalysisRtn.EVT_MEM_EFFECT_RTN evt_mem_effect_rtn = data_analysis_rtn.getEvtMemEffectRtn();
                                        JsonObject evtmemeffectrtn = new JsonObject();
                                        int optId = evt_mem_effect_rtn.getOptId();
                                        int evtId = evt_mem_effect_rtn.getEvtId();
                                        int mem_effect_sum = evt_mem_effect_rtn.getMemEffectSum();
                                        evtmemeffectrtn.put("optId",optId).put("evtId",evtId).put("mem_effect_sum",mem_effect_sum);


                                        List<AnalysisRtn.EVT_MEM_EFFECT_T> ele_effect = data_analysis_rtn.getEvtMemEffectRtn().getEleEffectList();
//                                    JsonArray eleefect= new JsonArray();
//                                    for(int i = 0; i <ele_effect.size(); i++){
//                                        JsonObject element = new JsonObject().put("objTag",ele_effect.get(i).getObjTag()).put("occurTimestamp",ele_effect.get(i).getOccurTimestamp()).put("obj_mem_effect",ele_effect.get(i).getObjMemEffect());
//                                        eleefect.add(element);
//                                    }
//                                    evtmemeffectrtn.put("optId",optId).put("evtId",evtId).put("mem_effect_sum",mem_effect_sum).put("ele_effect",eleefect);
//                                    dataanalysistn.put("evt_mem_effect_rtn",evtmemeffectrtn);
                                        for(int i = 0; i < ele_effect.size(); i ++){
                                            evtmemeffectrtn.put("objTag"+i, ele_effect.get(i).getObjTag())
                                                    .put("occurTimestamp"+i,ele_effect.get(i).getOccurTimestamp())
                                                    .put("obj_mem_effect"+i,ele_effect.get(i).getObjMemEffect());
                                        }
                                        String id = Tools.generateId();
                                        evtmemeffectrtn.put("portName",portName).put("id",id).put("timeStamp",timeStamp).put("type","特定空间操作对内存资源的影响").put("targetIP",targetIp);
                                        JsonObject searchObject = new JsonObject().put("taskID",taskIDExecuting).put("targetIP",targetIp).put("evtId",99990).put("type","ana").put("label","特定空间操作对内存资源的影响").put("timeStamp",timeStamp);
                                        String collectionName = targetIp+String.valueOf(99990);
                                        mongoClient.find("targetIPEvtId",searchObject,re->{
                                            if(re.failed()){
                                                logger.error(String.format("search targetEvtId: %s 查找失败", Tools.getTrace(re.cause())));
                                                return;
                                            }else if(re.result().size() == 0){

                                                searchObject.put("id",Tools.generateId());
                                                mongoClient.insert("targetIPEvtId",searchObject,rs->{
                                                    if(rs.failed()){
                                                        logger.error(String.format("new targetIPEvtId insert exception: %s", Tools.getTrace(rs.cause())));
                                                        return;
                                                    }
                                                    mongoClient.insert(collectionName,evtmemeffectrtn,rr->{
                                                        if(rr.failed()){
                                                            logger.error(String.format("%s insert exception: %s",targetGuid, Tools.getTrace(rr.cause())));
                                                            return;
                                                        }
                                                    });
                                                });
                                            }else {
                                                mongoClient.insert(collectionName,evtmemeffectrtn,rr->{
                                                    if(rr.failed()){
                                                        logger.error(String.format("%s insert exception: %s",targetGuid, Tools.getTrace(rr.cause())));
                                                        return;
                                                    }
                                                });

                                            }
                                        });

//                                    dataanalysistn.put("evt_mem_effect_rtn",evtmemeffectrtn);




//                                    JsonArray params = new JsonArray().add(data_analysis_rtn.getTargetGuid()).add("evt_mem_effect_rtn").add(data_analysis_rtn.getEvtMemEffectRtn());
//                                    insertMysql(sql,params);
                                    }
                                    if(data_analysis_rtn.hasTskMemEffectRtn()){
                                        AnalysisRtn.TSK_MEM_EFFECT_RTN tsk_mem_effect_rtn = data_analysis_rtn.getTskMemEffectRtn();
                                        JsonObject tskmemeffecttn = new JsonObject();
                                        int optId = tsk_mem_effect_rtn.getOptId();
                                        List< AnalysisRtn.PART_EFFECT> part_effect= tsk_mem_effect_rtn.getPartEffectList();
                                        JsonArray parteffect= new JsonArray();
//                                    for(int i = 0 ; i < part_effect.size(); i++){
//
//                                        JsonObject element = new JsonObject().put("pdId",tsk_mem_effect_rtn.getPartEffect(i).getPdId())
//                                                .put("partName",tsk_mem_effect_rtn.getPartEffect(i).getPartName())
//                                                .put("part_mem_effect",tsk_mem_effect_rtn.getPartEffect(i).getPartMemEffect());
//                                        List<AnalysisRtn.TASK_EFFECT> task_effect = tsk_mem_effect_rtn.getPartEffect(i).getTaskEffectList();
//                                        JsonArray taskeffect = new JsonArray();
//                                        for(int j=0; j < task_effect.size();j++){
//                                            JsonObject elementTask_effect = new JsonObject().put("taskId",tsk_mem_effect_rtn.getPartEffect(i).getTaskEffect(j).getTaskId())
//                                                    .put("taskName",tsk_mem_effect_rtn.getPartEffect(i).getTaskEffect(j).getTaskName())
//                                                    .put("task_mem_effect",tsk_mem_effect_rtn.getPartEffect(i).getTaskEffect(j).getTaskMemEffect());
//                                            List<AnalysisRtn.EVT_EFFECT> evt_effect = tsk_mem_effect_rtn.getPartEffect(i).getTaskEffect(j).getEvtEffectList();
//                                            JsonArray evfeffect= new JsonArray();
//                                            for(int z = 0 ;z <evt_effect.size();z ++){
//                                                JsonObject elementevt_efect = new JsonObject().put("evtId",tsk_mem_effect_rtn.getPartEffect(i).getTaskEffect(j).getEvtEffect(z).getEvtId())
//                                                        .put("evt_mem_effect",tsk_mem_effect_rtn.getPartEffect(i).getTaskEffect(j).getEvtEffect(z).getEvtMemEffect());
//                                                evfeffect.add(elementevt_efect);
//                                            }
//                                            elementTask_effect.put("evt_effect",evfeffect);
//                                            taskeffect.add(elementTask_effect);
//                                        }
//                                        element.put("task_effect",taskeffect);
//                                        parteffect.add(element);
//                                    }
                                        for(int i = 0; i <part_effect.size(); i ++){
                                            tskmemeffecttn.put("pdId"+i,tsk_mem_effect_rtn.getPartEffect(i).getPdId())
                                                    .put("partName"+i,tsk_mem_effect_rtn.getPartEffect(i).getPartName())
                                                    .put("part_mem_effect"+i,tsk_mem_effect_rtn.getPartEffect(i).getPartMemEffect());
                                            List<AnalysisRtn.TASK_EFFECT> task_effect = tsk_mem_effect_rtn.getPartEffect(i).getTaskEffectList();
                                            for(int j= 0; j <task_effect.size(); j ++){
                                                tskmemeffecttn.put("taskId"+i+j,task_effect.get(j).getTaskId())
                                                        .put("taskName"+i+j,task_effect.get(j).getTaskName())
                                                        .put("task_mem_effect"+i+j,task_effect.get(j).getTaskMemEffect());
                                                List<AnalysisRtn.EVT_EFFECT> evt_effect = task_effect.get(j).getEvtEffectList();
                                                for(int z= 0; z <evt_effect.size(); z++){
                                                    tskmemeffecttn.put("evtId"+i+j+z,evt_effect.get(z).getEvtId())
                                                            .put("evt_mem_effect"+i+j+z,evt_effect.get(z).getEvtMemEffect());
                                                }

                                            }

                                        }
                                        tskmemeffecttn.put("optId",optId);
                                        String id = Tools.generateId();
                                        tskmemeffecttn.put("id",id).put("portName",portName).put("timeStamp",timeStamp).put("type","任务及各种空间操作对内存资源的影响").put("targetIP",targetIp);
                                        JsonObject searchObject = new JsonObject().put("taskID",taskIDExecuting).put("targetIP",targetIp).put("evtId",99991).put("type","ana").put("label","任务及各种空间操作对内存资源的影响").put("timeStamp",timeStamp);
                                        String collectionName = targetIp+String.valueOf(99991);
                                        mongoClient.find("targetIPEvtId",searchObject,re->{
                                            if(re.failed()){
                                                logger.error(String.format("search targetEvtId: %s 查找失败", Tools.getTrace(re.cause())));
                                                return;
                                            }else if(re.result().size()==0){

                                                searchObject.put("id",Tools.generateId());
                                                mongoClient.insert("targetIPEvtId",searchObject,rs->{
                                                    if(rs.failed()){
                                                        logger.error(String.format("new targetIPEvtId insert exception: %s", Tools.getTrace(rs.cause())));
                                                        return;
                                                    }
                                                    mongoClient.insert(collectionName,tskmemeffecttn,r->{
                                                        if(r.failed()){
                                                            logger.error(String.format("%s insert exception: %s",targetGuid, Tools.getTrace(r.cause())));
                                                            return;
                                                        }
                                                    });
                                                });

                                            }else {
                                                mongoClient.insert(collectionName,tskmemeffecttn,r->{
                                                    if(r.failed()){
                                                        logger.error(String.format("%s insert exception: %s",targetGuid, Tools.getTrace(r.cause())));
                                                        return;
                                                    }
                                                });
                                            }
                                        });

//                                    dataanalysistn.put("tsk_mem_effect_rtn",tskmemeffecttn);



//                                    JsonArray params = new JsonArray().add(data_analysis_rtn.getTargetGuid()).add("tsk_mem_effect_rtn").add(data_analysis_rtn.getTskMemEffectRtn());
//                                    insertMysql(sql,params);
                                    }
                                    if(data_analysis_rtn.hasFuncTimeuseStaticRtn()){
                                        AnalysisRtn.FUNC_TIMEUSE_STATIC_RTN func_timeuse_static_rtn = data_analysis_rtn.getFuncTimeuseStaticRtn();
                                        JsonObject functimeusestaticrtn = new JsonObject().put("optId",func_timeuse_static_rtn.getOptId())
                                                .put("funcId",func_timeuse_static_rtn.getFuncId())
                                                .put("timeUse",func_timeuse_static_rtn.getTimeUse());
                                        String id = Tools.generateId();
                                        functimeusestaticrtn.put("id",id).put("portName",portName).put("timeStamp",timeStamp).put("type","特定函数的执行用时统计").put("targetIP",targetIp);
                                        JsonObject searchObject = new JsonObject().put("taskID",taskIDExecuting).put("targetIP",targetIp).put("evtId",99992).put("type","ana").put("label","特定函数的执行用时统计").put("timeStamp",timeStamp);
                                        String collectionName = targetIp+String.valueOf(99992);
                                        mongoClient.find("targetIPEvtId",searchObject,re->{
                                            if(re.failed()){
                                                logger.error(String.format("search targetEvtId: %s 查找失败", Tools.getTrace(re.cause())));
                                                return;
                                            }else if(re.result().size()==0){

                                                searchObject.put("id",Tools.generateId());
                                                mongoClient.insert("targetIPEvtId",searchObject,rs->{
                                                    if(rs.failed()){
                                                        logger.error(String.format("new targetIPEvtId insert exception: %s", Tools.getTrace(rs.cause())));
                                                        return;
                                                    }
                                                    mongoClient.insert(collectionName,functimeusestaticrtn,r->{
                                                        if(r.failed()){
                                                            logger.error(String.format("%s insert exception: %s",targetGuid, Tools.getTrace(r.cause())));
                                                            return;
                                                        }
                                                    });
                                                });
                                            }else {
                                                mongoClient.insert(collectionName,functimeusestaticrtn,r->{
                                                    if(r.failed()){
                                                        logger.error(String.format("%s insert exception: %s",targetGuid, Tools.getTrace(r.cause())));
                                                        return;
                                                    }
                                                });

                                            }
                                        });

//                                    dataanalysistn.put("func_timeuse_static_rtn",functimeusestaticrtn);

                                    }

                                    if(data_analysis_rtn.hasPartRuntimeAnalysisResult()){
                                        AnalysisRtn.PART_RUNTIME_ANALYSIS_RESULT func_timeuse_static_rtn = data_analysis_rtn.getPartRuntimeAnalysisResult();
                                        JsonObject partruntimeanalysisresult = new JsonObject().put("optId",func_timeuse_static_rtn.getOptId());
                                        List<AnalysisRtn.MF_RUNTIME_ANALYSIS_RESULT> mf_runtime_analysis_result = data_analysis_rtn.getPartRuntimeAnalysisResult().getMfRunTimeList();
//                                    JsonArray mfruntime = new JsonArray();
//                                    for(int i = 0; i<mf_runtime_analysis_result.size();i++){
//                                        JsonObject elementmfruntime = new JsonObject().put("schedId",data_analysis_rtn.getPartRuntimeAnalysisResult().getMfRunTime(i).getSchedId())
//                                                .put("timeStamp",data_analysis_rtn.getPartRuntimeAnalysisResult().getMfRunTime(i).getTimeStamp());
//                                        List<AnalysisRtn.PART_RUNTIME_DATA> part_runtime_data = data_analysis_rtn.getPartRuntimeAnalysisResult().getMfRunTime(i).getPartRunTimeDataList();
//                                        JsonArray partruntimedata = new JsonArray();
//                                        for(int j = 0 ; j <part_runtime_data.size();j++){
//                                            AnalysisRtn.PART_RUNTIME_DATA part_runtime_data1 = data_analysis_rtn.getPartRuntimeAnalysisResult().getMfRunTime(i).getPartRunTimeData(j);
//                                            JsonObject elementpartruntimedata = new JsonObject().put("pd",part_runtime_data1.getPd())
//                                                    .put("partName",part_runtime_data1.getPartName())
//                                                    .put("schedTime",part_runtime_data1.getSchedTime())
//                                                    .put("validTime",part_runtime_data1.getSchedTime())
//                                                    .put("remainTime",part_runtime_data1.getRemainTime());
//                                            List<AnalysisRtn.PART_TASK_RUNTIME> part_task_runtime = part_runtime_data1.getTaskInfoList();
//                                            JsonArray parttaskinfo = new JsonArray();
//                                            for(int z=0; z<parttaskinfo.size();z++){
//                                                JsonObject parttaskruntime = new JsonObject().put("taskID",part_runtime_data1.getTaskInfo(z).getTaskID())
//                                                        .put("taskName",part_runtime_data1.getTaskInfo(z).getTaskName())
//                                                        .put("taskRunTime",part_runtime_data1.getTaskInfo(z).getTaskRunTime());
//                                                parttaskinfo.add(parttaskruntime);
//                                            }
//                                            elementpartruntimedata.put("taskInfo",parttaskinfo);
//
//
//                                            partruntimedata.add(elementpartruntimedata);
//                                        }
//                                        elementmfruntime.put("partRunTimeData",partruntimedata);
//                                        mfruntime.add(elementmfruntime);
//
//                                    }
                                        for(int i = 0; i<mf_runtime_analysis_result.size();i++){
                                            partruntimeanalysisresult.put("schedId"+i,mf_runtime_analysis_result.get(i).getSchedId())
                                                    .put("timeStamp",mf_runtime_analysis_result.get(i).getTimeStamp());
                                            List<AnalysisRtn.PART_RUNTIME_DATA> part_runtime_data = mf_runtime_analysis_result.get(i).getPartRunTimeDataList();
                                            for(int j = 0; j<part_runtime_data.size();j++){
                                                partruntimeanalysisresult.put("pd"+i+j,part_runtime_data.get(j).getPd())
                                                        .put("partName"+i+j,part_runtime_data.get(j).getPartName())
                                                        .put("schedTime",part_runtime_data.get(j).getSchedTime())
                                                        .put("validTime",part_runtime_data.get(j).getValidTime())
                                                        .put("remainTime"+i+j,part_runtime_data.get(j).getRemainTime());
                                                List<AnalysisRtn.PART_TASK_RUNTIME> part_task_runtime = part_runtime_data.get(j).getTaskInfoList();
                                                for(int z= 0; z<part_task_runtime.size();z++){
                                                    partruntimeanalysisresult.put("taskID"+i+j+z,part_task_runtime.get(z).getTaskID())
                                                            .put("taskName"+i+j+z,part_task_runtime.get(z).getTaskName())
                                                            .put("taskRunTime"+i+j+z,part_task_runtime.get(z).getTaskRunTime());
                                                }

                                            }


                                        }
                                        String id = Tools.generateId();
                                        partruntimeanalysisresult.put("id",id).put("portName",portName).put("timeStamp",timeStamp).put("type","分区运行分析结果").put("targetIP",targetIp);
                                        JsonObject searchObject = new JsonObject().put("taskID",taskIDExecuting).put("targetIP",targetIp).put("evtId",99993).put("type","ana").put("label","分区运行分析结果").put("timeStamp",timeStamp);

                                        String colectionName = targetIp+String.valueOf(99993);
                                        mongoClient.find("targetIPEvtId",searchObject,re->{
                                            if(re.failed()){
                                                logger.error(String.format("search targetEvtId: %s 查找失败", Tools.getTrace(re.cause())));
                                                return;
                                            }else if(re.result().size() == 0){
                                                searchObject.put("id",Tools.generateId());
                                                mongoClient.insert("targetIPEvtId",searchObject,rs->{
                                                    if(rs.failed()){
                                                        logger.error(String.format("new targetIPEvtId insert exception: %s", Tools.getTrace(rs.cause())));
                                                        return;
                                                    }
                                                    mongoClient.insert(colectionName,partruntimeanalysisresult,r->{
                                                        if(r.failed()){
                                                            logger.error(String.format("%s insert exception: %s",targetGuid, Tools.getTrace(r.cause())));
                                                            return;
                                                        }

                                                    });
                                                });
                                            }else {
                                                mongoClient.insert(colectionName,partruntimeanalysisresult,r->{
                                                    if(r.failed()){
                                                        logger.error(String.format("%s insert exception: %s",targetGuid, Tools.getTrace(r.cause())));
                                                        return;
                                                    }

                                                });

                                            }
                                        });


//                                    partruntimeanalysisresult.put("mfRunTime",mfruntime);
//                                    dataanalysistn.put("part_runtime_analysis_result",partruntimeanalysisresult);


//                                    JsonArray params = new JsonArray().add(data_analysis_rtn.getTargetGuid()).add("part_runtime_analysis_result").add(data_analysis_rtn.getPartRuntimeAnalysisResult());
//                                    insertMysql(sql,params);
                                    }
                                    if(data_analysis_rtn.hasTaskRuntimeAnalysisResult()){
                                        AnalysisRtn.TASK_RUNTIME_ANALYSIS_RESULT task_runtime_analysis_result = data_analysis_rtn.getTaskRuntimeAnalysisResult();
                                        JsonObject taskruntimeanalysisresult = new JsonObject().put("optId",task_runtime_analysis_result.getOptId());
                                        List<AnalysisRtn.TASK_RUNTIME_INFO> task_runtime_info = task_runtime_analysis_result.getTaskRunInfoList();
//                                    JsonArray taskruntimeinfo = new JsonArray();
                                        for(int i = 0 ; i <task_runtime_info.size();i++){

                                            taskruntimeanalysisresult.put("taskID"+i,task_runtime_info.get(i).getTaskID())
                                                    .put("taskName"+i,task_runtime_info.get(i).getTaskName())
                                                    .put("runTime"+i,task_runtime_info.get(i).getRunTime());

                                        }
                                        String id = Tools.generateId();
                                        taskruntimeanalysisresult.put("id",id).put("portName",portName).put("timeStamp",timeStamp).put("type","任务运行分析结果").put("targetIP",targetIp);
                                        JsonObject searchObjet = new JsonObject().put("taskID",taskIDExecuting).put("targetIP",targetIp).put("evtId",99994).put("type","ana").put("label","任务运行分析结果").put("timeStamp",timeStamp);
                                        String collectionName = targetIp+String.valueOf(99994);
                                        mongoClient.find("targetEvtId",searchObjet,re->{
                                            if(re.failed()){
                                                logger.error(String.format("search targetEvtId: %s 查找失败", Tools.getTrace(re.cause())));
                                                return;
                                            }else if(re.result().size() == 0){
                                                searchObjet.put("id",Tools.generateId());
                                                mongoClient.insert("targetEvtId",searchObjet,rs->{
                                                    if(rs.failed()){
                                                        logger.error(String.format("new targetIPEvtId insert exception: %s", Tools.getTrace(rs.cause())));
                                                        return;
                                                    }
                                                    mongoClient.insert(collectionName,taskruntimeanalysisresult,r->{
                                                        if(r.failed()){
                                                            logger.error(String.format("%s insert exception: %s",targetGuid, Tools.getTrace(r.cause())));
                                                            return;
                                                        }
                                                    });
                                                });
                                            }else {
                                                mongoClient.insert(collectionName,taskruntimeanalysisresult,r->{
                                                    if(r.failed()){
                                                        logger.error(String.format("%s insert exception: %s",targetGuid, Tools.getTrace(r.cause())));
                                                        return;
                                                    }
                                                });
                                            }
                                        });



//                                    taskruntimeanalysisresult.put("taskRunInfo",taskruntimeinfo);
//                                    dataanalysistn.put("task_runtime_analysis_result",taskruntimeanalysisresult);
//                                    JsonArray params = new JsonArray().add(data_analysis_rtn.getTargetGuid()).add("task_runtime_analysis_result").add(data_analysis_rtn.getTaskRuntimeAnalysisResult());
//                                    insertMysql(sql,params);
                                    }
                                    if(data_analysis_rtn.hasPartSectionSizeResult()){
                                        AnalysisRtn.PART_SECTION_SIZE_RESULT part_section_size_result = data_analysis_rtn.getPartSectionSizeResult();
                                        JsonObject partsectionsizeresult = new JsonObject().put("optId",part_section_size_result.getOptId());
                                        List< AnalysisRtn.PART_SECTION_SIZE_INFO> part_section_size_info = part_section_size_result.getPartSecSizeInfoList();
//                                    JsonArray partsectionsizersult  = new JsonArray();
//                                    for(int i =0; i<part_section_size_info.size();i++){
//                                        JsonObject elementpartsectionsizeinfo = new JsonObject().put("partName",part_section_size_info.get(i).getPartName());
//                                        List sectionInfolist = part_section_size_info.get(i).getSectionInfoList();
//                                        JsonArray sectioninfo = new JsonArray();
//                                        for(int j = 0 ; j <sectionInfolist.size();j++){
//                                            Object elementsectioninfo = sectionInfolist.get(j);
//                                            sectioninfo.add(elementsectioninfo);
//                                        }
//                                        elementpartsectionsizeinfo.put("sectionInfo",sectioninfo);
//                                        partsectionsizersult.add(elementpartsectionsizeinfo);
//                                    }
                                        for(int i = 0; i <part_section_size_info.size();i ++){
                                            partsectionsizeresult.put("partName"+i,part_section_size_info.get(i).getPartName());
                                            List<AnalysisRtn.SECTION_SIZE_INFO> sectionInfolist = part_section_size_info.get(i).getSectionInfoList();
                                            for(int j = 0; j < sectionInfolist.size(); j++){
                                                partsectionsizeresult.put("sectionName"+i+j,sectionInfolist.get(j).getSectionName())
                                                        .put("allocSize"+i+j,sectionInfolist.get(j).getAllocSize())
                                                        .put("actualSize"+i+j,sectionInfolist.get(j).getActualSize());
                                            }

                                        }

                                        String id = Tools.generateId();
                                        partsectionsizeresult.put("id",id).put("portName",portName).put("timeStamp",timeStamp).put("type","分区的各段的大小分析结果").put("targetIP",targetIp);
                                        JsonObject searchObject  = new JsonObject().put("taskID",taskIDExecuting).put("targetIP",targetIp).put("evtId",99995).put("type","ana").put("label","分区的各段的大小分析结果").put("timeStamp",timeStamp);
                                        String collectionName = targetIp+String.valueOf(99995);
                                        mongoClient.find("targetIPEvtId",searchObject,re->{
                                                    if(re.failed()){
                                                        logger.error(String.format("search targetEvtId: %s 查找失败", Tools.getTrace(re.cause())));
                                                        return;
                                                    }else if(re.result().size()== 0){
                                                        searchObject.put("id",Tools.generateId());
                                                        mongoClient.insert("targetIPEvtId",searchObject,rs->{
                                                            if(rs.failed()){
                                                                logger.error(String.format("new targetIPEvtId insert exception: %s", Tools.getTrace(rs.cause())));
                                                                return;
                                                            }
                                                            mongoClient.insert(collectionName,partsectionsizeresult,r->{
                                                                if(r.failed()){
                                                                    logger.error(String.format("%s insert exception: %s",targetGuid, Tools.getTrace(r.cause())));
                                                                    return;
                                                                }
                                                            });
                                                        });
                                                    }else {
                                                        mongoClient.insert(collectionName,partsectionsizeresult,r->{
                                                            if(r.failed()){
                                                                logger.error(String.format("%s insert exception: %s",targetGuid, Tools.getTrace(r.cause())));
                                                                return;
                                                            }
                                                        });
                                                    }
                                                }
                                        );


//                                    partsectionsizeresult.put("partSecSizeInfo",partsectionsizersult);
//                                    dataanalysistn.put("part_section_size_result",partsectionsizeresult);
                                    }





                                    break;
                                case 5:
                                    byte[] bytes = udppacket.getBuffers().toByteArray();//获取二进制文件
                                    Future<JsonObject> future = Future.future();
                                    mongoClient.find("ICD",new JsonObject(),r->{//获取最新的ICD
                                        if (r.failed()){
                                            logger.error(String.format("ICD query exception: %s",Tools.getTrace(r.cause())));
                                            return;
                                        }else {
                                            future.complete(r.result().get(0).getJsonObject("ICD"));
                                        }
                                    });
                                    future.setHandler(r->{
                                        ICDParse icdParse = new ICDParse();
                                        Map<String,Object> map = icdParse.jsonToMap(future.result());
                                        Set<String> nameSet = map.keySet();//
                                        Iterator iterator = nameSet.iterator();
                                        String icdName = iterator.next().toString();//获取第一层icd的key
                                        Object firstICDObject = map.get(icdName);//获取第一层的对象
                                        JsonObject firstResult = new JsonObject();
                                        JsonObject result = new JsonObject();

                                        if(firstICDObject != null){
//            Map<String,Object>  secondICDMap= new org.apache.commons.beanutils.BeanMap(firstICDObject);//第二层ICD的Map
                                            if(firstICDObject instanceof Map == false){
                                                socket.send(String.format("packet: %ld, exception:%s",timeStamp,"请求解析错误"),packet.sender().port(),packet.sender().host(),rc->{
                                                    logger.error(String.format("exception:%s","请求解析错误"));
                                                    return;
                                                });

                                            }
                                            Map<String,Object>  secondICDMap = (Map<String,Object>) firstICDObject;
                                            JsonObject secondResult = null;
                                            for(Map.Entry<String,Object> entry : secondICDMap.entrySet()){//遍历第二层ICD元素

                                                String secondICDKey = entry.getKey();//第二层
                                                Object secondICDObject = entry.getValue();

                                                if(secondICDObject instanceof Map){
                                                    Map<String,Object> thirdICDMap = (Map<String,Object>)secondICDObject;//第三层ICD
                                                    if(thirdICDMap.containsKey("_portName") ){

                                                        String thirdICDPortName = thirdICDMap.get("_portName").toString();
                                                        if(thirdICDPortName.equals(portName)){//寻找与二进制数据对应的portName
                                                            secondResult= icdParse.portNameParse(thirdICDMap,secondICDKey,0,bytes);
                                                        }else {
                                                            continue;
                                                        }
                                                    }else {
                                                        socket.send(String.format("packet: %ld, exception:%s",timeStamp,"请求解析错误"),packet.sender().port(),packet.sender().host(),rd->{
                                                            logger.error(String.format("packet: %ld, exception:%s",timeStamp,"请求解析错误"));
                                                            return;
                                                        });
                                                    }
                                                }else {
                                                    continue;
                                                }
                                                firstResult.put(secondICDKey,secondResult);
                                            }
                                            result.put(icdName,firstResult);
                                            mongoClient.insert("icdParsedData",result,rm->{
                                                if(rm.failed()){
                                                    socket.send(String.format("packet: %ld, exception:%s",timeStamp,"数据持久化错误"),packet.sender().port(),packet.sender().host(),rb->{
                                                        logger.error(String.format("packet: %ld, exception:%s",timeStamp,"数据持久化错误"));
                                                        return;
                                                    });

                                                }else {
                                                    socket.send(result.toString(),packet.sender().port(),packet.sender().host(),rs->{
                                                        logger.info(String.format("packet: %ld, info:%s",timeStamp,"请求解析成功"));
                                                        return;
                                                    });
                                                }
                                            });
//                                        socket.send(result.toString(),packet.sender().port(),packet.sender().host(),rf->{
//
//                                        });
//                                        logger.info(String.format("info:%s",""));
//                                        return;

                                        }else {
                                            socket.send(String.format("packet: %ld, exception:%s",timeStamp,"请求解析错误"),packet.sender().port(),packet.sender().host(),ra->{
                                                logger.error(String.format("packet: %ld, exception:%s",timeStamp,"请求解析错误"));
                                                return;
                                            });

                                        }

                                    });
//                            case 7://数据激励
//                                String feedBack = null;
//                                try {
//                                    feedBack = udppacket.getBuffers().toString("GB2312");
//                                    if(feedBack.toUpperCase().indexOf("success".toUpperCase())<0){
//                                        mongoClient.find("targetIPPortID",new JsonObject(),r->{
//                                            if(r.failed()){
//                                                logger.info(String.format("query %s, exception:%s","targetIP+portID",Tools.getTrace(r.cause())));
//                                                return;
//                                            }else {
//                                                String targetIP = r.result().get(0).getString("targetIP");
//                                                int portID = r.result().get(0).getInteger("portID");
//                                                Object data = r.result().get(0).getJsonArray("data");
//
//
//                                            }
//                                        });
//
//
//
//
//                                    }else {
//                                        logger.error(String.format("数据激励失败"));
//                                        return;
//                                    }
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                }





                                    //调用ICD分析功能

                                default:
                                    //无
                            }

                        });

                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

//        logger.info(String.format("please visit url: http://%s:%d%s", this.host, this.port, "/"));
    }

}
