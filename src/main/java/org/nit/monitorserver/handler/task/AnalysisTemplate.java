package org.nit.monitorserver.handler.task;

import com.google.protobuf.ByteString;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;

import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.GlobalConsts;

import org.nit.monitorserver.database.MongoConnection;

//import org.nit.monitorserver.proto.AnalysisCfg;
//import org.nit.monitorserver.proto.AnalysisCfg;
import org.nit.monitorserver.proto.UdpPacket;
import org.nit.monitorserver.proto.AnalysisCfg;
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.IniUtil;
import org.nit.monitorserver.util.Tools;

import java.util.ArrayList;
import java.util.List;

import static org.nit.monitorserver.constant.ResponseError.FORMAT_ERROR;
import static org.nit.monitorserver.constant.ResponseError.PARAM_REQUIRED;
import static org.nit.monitorserver.constant.ResponseError.QUERY_FAILURE;
/**
 * 功能描述: <和CreateTask合并>
 * 〈〉
 * @Author: 20643
 * @Date: 2020-9-1 14:31
 */

public class AnalysisTemplate {

    private Vertx vertx=Vertx.vertx();
    public static String targetIP = null;
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    private int port= IniUtil.getInstance().getUdpServerPort();
    private String host=IniUtil.getInstance().getUdpServerHost();



    public Future<JsonObject> EDT_DATA_ANALYSIS_CFG(String targetIP, Logger logger){
        JsonObject condition = new JsonObject().put("targetIP",targetIP);
        JsonObject analysisRtn = new JsonObject();
        Future<JsonObject> future = Future.future();


        FindOptions options = new FindOptions().setSort(new JsonObject().put("timeStamp",-1)).setLimit(1);//获取最新一则数据,-1降序，1升序
        mongoClient.findWithOptions("analysis_record",condition,options,r->{
            JsonObject response = new JsonObject();
            if(r.failed()){
                logger.error(String.format("mongodb find analysisRtn data error: %s", Tools.getTrace(r.cause())));
                analysisRtn.put("type","error").put("code",QUERY_FAILURE.getCode()).put("message",QUERY_FAILURE.getMsg());

////                System.out.println(sql);
//                response.error(ResponseError.SERVER_ERROR.getCode(), ResponseError.SERVER_ERROR.getMsg());
//                return;
            }else{
                analysisRtn.put("EDT_DATA_ANALYSIS_CFG",r.result());
            }
            future.complete(analysisRtn);

        });

        return future;


    }

    public JsonObject analysisParse(Object contentObject,Object flag,Object targetIPObject, Logger logger){
        JsonObject response = new JsonObject();
        if (contentObject == null) {
            logger.error(String.format("exception: %s", "分析配置为必填参数"));
            response.put("type", "error").put("code", PARAM_REQUIRED.getCode()).put("message", PARAM_REQUIRED.getMsg());
            return response;
        } else if (!FormValidator.isJsonObject(contentObject)) {
            logger.error(String.format("exception: %s", "分析配置格式错误"));
            response.put("type", "error").put("code", FORMAT_ERROR.getCode()).put("message", FORMAT_ERROR.getMsg());
//            response.error(FORMAT_ERROR.getCode(), FORMAT_ERROR.getMsg());
            return response;
        }
        JsonObject content =(JsonObject) contentObject;
        if(!content.containsKey("EDT_DATA_ANALYSIS_CFG")){
            logger.error(String.format("exception: %s","分析配置为必填参数"));
            response.put("type","error").put("code",PARAM_REQUIRED.getCode()).put("message",PARAM_REQUIRED.getMsg());
//            response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
            return response;
        }

        if(targetIPObject == null){
            logger.error(String.format("exception: %s","目标机为必填参数"));
            response.put("type","error").put("code",PARAM_REQUIRED.getCode()).put("message",PARAM_REQUIRED.getMsg());
//            response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
            return response;
        }else if(!FormValidator.isString(targetIPObject)){
            logger.error(String.format("exception: %s","目标机格式错误"));
            response.put("type","error").put("code",FORMAT_ERROR.getCode()).put("message",FORMAT_ERROR.getMsg());
//            response.error(FORMAT_ERROR.getCode(),FORMAT_ERROR.getMsg());
            return response;
        }
        targetIP = targetIPObject.toString();


        JsonObject EDT_DATA_ANALYSIS_CFG = content.getJsonObject("EDT_DATA_ANALYSIS_CFG");


//        JsonObject EDT_DATA_ANALYSIS_CFG = content.getJsonObject("EDT_DATA_ANALYSIS_CFG");
//        if(EDT_DATA_ANALYSIS_CFG.containsKey("targetGuid")){
//            targetGuid = EDT_DATA_ANALYSIS_CFG.getString("targetGuid");
//        }



        if(flag.toString().equals("false")){
            Future<JsonObject> future  = EDT_DATA_ANALYSIS_CFG(targetIP,logger);
            List<JsonObject> list = new ArrayList();
            future.setHandler(r->{
                response.put("type","success").put("result",future.result());
                list.add(response);
            });
            while (list.size() != 1){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return list.get(0);
//            response.put("type","success").put("result",EDT_DATA_ANALYSIS_CFG(targetIP,logger));
//            return response;



        }else if(flag.toString().equals("true")){
            AnalysisCfg.EDT_DATA_ANALYSIS_CFG.Builder edt_data_analysis_cfg = AnalysisCfg.EDT_DATA_ANALYSIS_CFG.newBuilder();

            if(content.containsKey("EDT_DATA_ANALYSIS_CFG")){


                edt_data_analysis_cfg.setTargetGuid(EDT_DATA_ANALYSIS_CFG.getString(GlobalConsts.TARGERGUID));
                if(EDT_DATA_ANALYSIS_CFG.containsKey("EVT_MEM_EFFECT_CFG")){//特定空间操作对内存资源的影响
                    AnalysisCfg.EVT_MEM_EFFECT_CFG.Builder evt_mem_effet_cgf =AnalysisCfg.EVT_MEM_EFFECT_CFG.newBuilder();//特定空间操作对内存资源的影响
                    JsonObject EVT_MEM_EFFECT_CFG = EDT_DATA_ANALYSIS_CFG.getJsonObject("EDT_DATA_ANALYSIS_CFG");
                    if(EVT_MEM_EFFECT_CFG.containsKey("optId")){//操作编号
                        evt_mem_effet_cgf.setOptId(Integer.parseInt((String)EVT_MEM_EFFECT_CFG.getValue(GlobalConsts.OPTID)));
                    }
                    if(EVT_MEM_EFFECT_CFG.containsKey("evtId")){//待分析事件的ID
                        evt_mem_effet_cgf.setEvtId(Integer.parseInt((String)EVT_MEM_EFFECT_CFG.getValue(GlobalConsts.EVTID)));
                    }
                    if(EVT_MEM_EFFECT_CFG.containsKey("pdId")){ //待分析事件所在分区pd
                        evt_mem_effet_cgf.setPdId(Integer.parseInt((String)EVT_MEM_EFFECT_CFG.getValue(GlobalConsts.PDMEMID)));
                    }
                    if(EVT_MEM_EFFECT_CFG.containsKey("startTime")){//分析的起始时间
                        evt_mem_effet_cgf.setStartTime(Integer.parseInt((String)EVT_MEM_EFFECT_CFG.getValue(GlobalConsts.START_TIME)));
                    }
                    if(EVT_MEM_EFFECT_CFG.containsKey("endTime")){  //*分析的结束时间
                        evt_mem_effet_cgf.setEndTime(Integer.parseInt((String)EVT_MEM_EFFECT_CFG.getValue(GlobalConsts.END_TIME)));
                    }
                    edt_data_analysis_cfg.setEvtMemEffectCfg(evt_mem_effet_cgf);
                }
                if(EDT_DATA_ANALYSIS_CFG.containsKey("TSK_MEM_EFFECT_CFG")){//任务及各种空间操作对内存资源的影响
                    JsonObject TSK_MEM_EFFECT_CFG = EDT_DATA_ANALYSIS_CFG.getJsonObject("TSK_MEM_EFFECT_CFG");
                    AnalysisCfg.TSK_MEM_EFFECT_CFG.Builder tsk_mem_effet_cfg =AnalysisCfg.TSK_MEM_EFFECT_CFG.newBuilder();//任务及各种空间操作对内存资源的影响
                    if(TSK_MEM_EFFECT_CFG.containsKey("optId")){
                        tsk_mem_effet_cfg.setOptId(Integer.parseInt((String)TSK_MEM_EFFECT_CFG.getValue(GlobalConsts.OPTID)));
                    }
                    if(TSK_MEM_EFFECT_CFG.containsKey("pdId")){
                        tsk_mem_effet_cfg.setPdId(Integer.parseInt((String)TSK_MEM_EFFECT_CFG.getValue(GlobalConsts.PDMEMID)));
                    }
                    if(TSK_MEM_EFFECT_CFG.containsKey("startTime")){
                        tsk_mem_effet_cfg.setStartTime(Integer.parseInt((String)TSK_MEM_EFFECT_CFG.getValue(GlobalConsts.START_TIME)));
                    }
                    if(TSK_MEM_EFFECT_CFG.containsKey("endTime")){
                        tsk_mem_effet_cfg.setEndTime(Integer.parseInt((String)TSK_MEM_EFFECT_CFG.getValue(GlobalConsts.END_TIME)));
                    }
                    edt_data_analysis_cfg.setTskMemEffectCfg(tsk_mem_effet_cfg);
                }
                if(EDT_DATA_ANALYSIS_CFG.containsKey("FUNC_TIMEUSE_STATIC")){ //特定函数执行用时
                    JsonObject FUNC_TIMEUSE_STATIC = EDT_DATA_ANALYSIS_CFG.getJsonObject("FUNC_TIMEUSE_STATIC");
                    AnalysisCfg.FUNC_TIMEUSE_STATIC.Builder func_imeuse_static =AnalysisCfg.FUNC_TIMEUSE_STATIC.newBuilder();//任务及各种空间操作对内存资源的影响
                    if(FUNC_TIMEUSE_STATIC.containsKey("optId")){
                        func_imeuse_static.setOptId(Integer.parseInt((String)FUNC_TIMEUSE_STATIC.getValue(GlobalConsts.OPTID)));
                    }
                    if(FUNC_TIMEUSE_STATIC.containsKey("pdId")){
                        func_imeuse_static.setPdId(Integer.parseInt((String)FUNC_TIMEUSE_STATIC.getValue(GlobalConsts.PDMEMID)));
                    }
                    if(FUNC_TIMEUSE_STATIC.containsKey("funcId")){
                        func_imeuse_static.setFuncId(Integer.parseInt((String)FUNC_TIMEUSE_STATIC.getValue(GlobalConsts.FUNCID)));
                    }
                    if(FUNC_TIMEUSE_STATIC.containsKey("startTime")){
                        func_imeuse_static.setStartTime(Integer.parseInt((String)FUNC_TIMEUSE_STATIC.getValue(GlobalConsts.START_TIME)));
                    }
                    if(FUNC_TIMEUSE_STATIC.containsKey("endTime")){
                        func_imeuse_static.setEndTime(Integer.parseInt((String)FUNC_TIMEUSE_STATIC.getValue(GlobalConsts.END_TIME)));
                    }
                    edt_data_analysis_cfg.setFuncTimeuseStatic(func_imeuse_static);
                }
                if(EDT_DATA_ANALYSIS_CFG.containsKey("PART_RUNTIME_ANALYSIS_CFG")){//分区运行分析请求
                    JsonObject PART_RUNTIME_ANALYSIS_CFG = EDT_DATA_ANALYSIS_CFG.getJsonObject("PART_RUNTIME_ANALYSIS_CFG");
                    AnalysisCfg.PART_RUNTIME_ANALYSIS_CFG.Builder part_runtim_analysis_cfg =AnalysisCfg.PART_RUNTIME_ANALYSIS_CFG.newBuilder();//任务及各种空间操作对内存资源的影响
                    if(PART_RUNTIME_ANALYSIS_CFG.containsKey("optId")){
                        part_runtim_analysis_cfg.setOptId(Integer.parseInt((String)PART_RUNTIME_ANALYSIS_CFG.getValue(GlobalConsts.OPTID)));
                    }
                    if(PART_RUNTIME_ANALYSIS_CFG.containsKey("startTime")){
                        part_runtim_analysis_cfg.setStartTime(Integer.parseInt((String)PART_RUNTIME_ANALYSIS_CFG.getValue(GlobalConsts.START_TIME)));
                    }
                    if(PART_RUNTIME_ANALYSIS_CFG.containsKey("endTime")){
                        part_runtim_analysis_cfg.setEndTime(Integer.parseInt((String)PART_RUNTIME_ANALYSIS_CFG.getValue(GlobalConsts.END_TIME)));
                    }
                    edt_data_analysis_cfg.setPartRuntimeCfg(part_runtim_analysis_cfg);

                }
                if(EDT_DATA_ANALYSIS_CFG.containsKey("TASK_RUNTIME_ANALYSIS_CFG")){//任务运行分析请求
                    JsonObject TASK_RUNTIME_ANALYSIS_CFG = EDT_DATA_ANALYSIS_CFG.getJsonObject("TASK_RUNTIME_ANALYSIS_CFG");
                    AnalysisCfg.TASK_RUNTIME_ANALYSIS_CFG.Builder task_runtime_analysis_cfg =AnalysisCfg.TASK_RUNTIME_ANALYSIS_CFG.newBuilder();//任务及各种空间操作对内存资源的影响
                    if(TASK_RUNTIME_ANALYSIS_CFG.containsKey("optId")){
                        task_runtime_analysis_cfg.setOptId(Integer.parseInt((String)TASK_RUNTIME_ANALYSIS_CFG.getValue(GlobalConsts.OPTID)));
                    }
                    if(TASK_RUNTIME_ANALYSIS_CFG.containsKey("startTime")){
                        task_runtime_analysis_cfg.setStartTime(Integer.parseInt((String)TASK_RUNTIME_ANALYSIS_CFG.getValue(GlobalConsts.START_TIME)));
                    }
                    if(TASK_RUNTIME_ANALYSIS_CFG.containsKey("endTime")){
                        task_runtime_analysis_cfg.setEndTime(Integer.parseInt((String)TASK_RUNTIME_ANALYSIS_CFG.getValue(GlobalConsts.END_TIME)));
                    }
                    edt_data_analysis_cfg.setTaskRuntimeCfg(task_runtime_analysis_cfg);

                }
                UdpPacket.UDP_COMMUNCATE_PACKET.Builder udpPacket=UdpPacket.UDP_COMMUNCATE_PACKET.newBuilder();//构建udp数据包
                udpPacket.setTimeStamp(System.currentTimeMillis()) //时间戳
                        .setMsgType(1)//消息类型
                        .setIpAddr(targetIP) ;//ip地址
//                HdpMsg.EDT_TDR_CFGCTR_MSG edttdrcfgctrmsg=edt_tdr_cfgctr_msg.build();
                AnalysisCfg.EDT_DATA_ANALYSIS_CFG edtdataanalysiscfg = edt_data_analysis_cfg.build();
//                System.out.println(edttdrcfgctrmsg);
                System.out.println(edtdataanalysiscfg);
//            byte[] udpBuffers=cmd.toByteArray();
//                udpPacket.setCurrLength(edttdrcfgctrmsg.toByteString().size()).setTotalLength(edttdrcfgctrmsg.toByteString().size()).setBuffers(edttdrcfgctrmsg.toByteString());
                udpPacket.setCurrLength(edtdataanalysiscfg.toByteString().size()).setTotalLength(edtdataanalysiscfg.toByteString().size()).setBuffers(edtdataanalysiscfg.toByteString());

                ByteString test=udpPacket.build().getBuffers();
                System.out.println(udpPacket.build().getBuffers());
//            System.out.println(HdpMsg.EDT_TDR_CFGCTR_MSG.parseFrom(cmd.toByteString()));
                DatagramSocket commandSendsocket= vertx.createDatagramSocket();
                Future sendCommandFuture=Future.future();

//            System.out.println(Buffer.buffer(udpPacket.build().toByteArray()));
                byte[] buffer=udpPacket.build().toByteArray();
//            udpPacket.
                commandSendsocket.send(Buffer.buffer(udpPacket.build().toByteArray()),port,host, asyncResult->{
                    if(asyncResult.succeeded()){
                        sendCommandFuture.complete();
                        logger.info("Command send success!");
                    }
                });

                List<JsonObject> list1 = new ArrayList();
                List<Future<JsonObject>> futureList = new ArrayList<>();

                sendCommandFuture.setHandler(asyn->{

                     futureList.add(EDT_DATA_ANALYSIS_CFG(targetIP,logger));
                     futureList.get(0).setHandler(r->{
                        response.put("type","success").put("result", futureList.get(0).result());
                        list1.add(response);
                    });


                });
                while (list1.size() != 1){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                return list1.get(0);
            }

        }
        return null;

    }
}

