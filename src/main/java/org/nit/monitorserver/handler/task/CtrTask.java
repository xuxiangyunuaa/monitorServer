package org.nit.monitorserver.handler.task;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.database.MysqlConnection;
import org.nit.monitorserver.handler.log.CreateLog;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.proto.AnalysisCfg;
import org.nit.monitorserver.proto.HdpMsg;
import org.nit.monitorserver.proto.UdpPacket;
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.IniUtil;
import org.nit.monitorserver.util.Tools;
import sun.awt.geom.AreaOp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @ClassName CtrTask
 * @Description TODO控制采集任务
 * @Author 20643
 * @Date 2020-9-1 14:38
 * @Version 1.0
 **/
public class CtrTask extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(CtrTask.class);
    //    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    private Vertx vertx = Vertx.vertx();
    private int port = IniUtil.getInstance().getDacmdPort();
    private String host = IniUtil.getInstance().getUdpServerHost();
    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    String query = "SELECT * FROM detrcd_raws_table; ";
    long timerID = Long.MIN_VALUE;
    CreateLog createLog = new CreateLog();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);


        HdpMsg.CTR_CMD_ST.Builder ctr_cmd_st = HdpMsg.CTR_CMD_ST.newBuilder();
        DatagramSocket commandSendsocket = vertx.createDatagramSocket();

        Object idObject = request.getParams().getValue("id");
        Object runFlagObject = request.getParams().getValue("runFlag");
        Future futureCom  = Future.future();
        Future futureAna  = Future.future();

        //id
        if(idObject == null || idObject.toString().equals("")){
            logger.error(String.format("ctr task exception: %s", "采集任务id为必填参数"));
            response.error(TASKID.getCode(), TASKID.getMsg());
            createLog.createLogRecord("任务管理","error","控制任务","采集任务id为必填参数");
            return;
        }
        if(!FormValidator.isString(idObject)){
            logger.error(String.format("ctr task exception: %s", "采集任务id格式错误"));
            response.error(TASKID_FORMAT_ERROR.getCode(), TASKID_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("任务管理","error","控制任务","采集任务id格式错误");
            return;
        }
        String id = idObject.toString();

        JsonObject searchObject = new JsonObject().put("id",id);

        //配置控制事件
        int cfgCtrId = 0;
        ctr_cmd_st.setCfgCtrId(cfgCtrId);

        //runFlag
        if(runFlagObject == null){
            logger.error(String.format("ctr exception: %s", "运行标识为必填参数"));
            createLog.createLogRecord("任务管理","error","控制任务","运行标识为必填参数");
            response.error(RUNFLAG.getCode(), RUNFLAG.getMsg());
            return;
        }
        if(!FormValidator.isInteger(runFlagObject)){
            logger.error(String.format("ctr exception: %s", "运行标识格式错误"));
            response.error(RUNFLAG_FORMAT_ERROR.getCode(), RUNFLAG_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("任务管理","error","控制任务","运行标识格式错误");
            return;
        }
        int runFlag = (int) runFlagObject;
        if(runFlag != 0 && runFlag != 1){
            logger.error(String.format("ctr exception: %s", "运行标识格式错误"));
            response.error(RUNFLAG_FORMAT_ERROR.getCode(), RUNFLAG_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("任务管理","error","控制任务","运行标识格式错误");
            return;
        }
        ctr_cmd_st.setRunFlag(runFlag);
        System.out.println("运行标识为："+runFlag);

//        //apexFlag
//        Object apexFlagObject = request.getParams().getValue("apexFlag");
//        if(apexFlagObject != null){
//            if(!FormValidator.isInteger(apexFlagObject)){
//                logger.error(String.format("ctr exception: %s", "分区apex信息采集格式错误"));
//                response.error(APEXFLAG_FORMAT_ERROR.getCode(), APEXFLAG_FORMAT_ERROR.getMsg());
//                return;
//            }
//            int apexFlag = (int) apexFlagObject;
//            if(apexFlag != 0 && apexFlag != 1){
//                logger.error(String.format("ctr exception: %s", "分区apex信息采集格式错误"));
//                response.error(APEXFLAG_FORMAT_ERROR.getCode(), APEXFLAG_FORMAT_ERROR.getMsg());
//                return;
//            }
//            ctr_cmd_st.setApexFlag(apexFlag);
//
//        }
//
//        //posixFlag
//        Object posixFlagObject = request.getParams().getValue("posixFlag");
//        if(posixFlagObject != null){
//            if(!FormValidator.isInteger(posixFlagObject)){
//                logger.error(String.format("ctr exception: %s", "分区posix信息采集格式错误"));
//                response.error(POSIXFLAG_FORMAT_ERROR.getCode(), POSIXFLAG_FORMAT_ERROR.getMsg());
//                return;
//            }
//            int posixFlag = (int) posixFlagObject;
//            if(posixFlag != 0 && posixFlag != 1){
//                logger.error(String.format("ctr exception: %s", "分区posix信息采集格式错误"));
//                response.error(POSIXFLAG_FORMAT_ERROR.getCode(), POSIXFLAG_FORMAT_ERROR.getMsg());
//                return;
//            }
//            ctr_cmd_st.setPosixFlag(posixFlag);
//
//        }
//
//        //angFlag
//        int anaFlag = -1;
//        Object anaFlagObject = request.getParams().getValue("anaFlag");
//        if(anaFlagObject != null){
//            if(!FormValidator.isInteger(anaFlagObject)){
//                logger.error(String.format("ctr exception: %s", "玖翼部分的分析数据格式错误"));
//                response.error(ANAFLAG_FORMAT_ERROR.getCode(), ANAFLAG_FORMAT_ERROR.getMsg());
//                return;
//            }
//            anaFlag = (int) anaFlagObject;
//            if(anaFlag != 0 && anaFlag != 1){
//                logger.error(String.format("ctr exception: %s", "玖翼部分的分析数据格式错误"));
//                response.error(ANAFLAG_FORMAT_ERROR.getCode(), ANAFLAG_FORMAT_ERROR.getMsg());
//                return;
//            }
//        }

        System.out.println("参数验证合格");
       //检查是否存在其他启动任务
        mongoClient.find("task",new JsonObject().put("runFlag",1),r->{
            System.out.println("开始查找是否存在启动任务");
            if(r.failed()){
                logger.error(String.format("search 正在运行中的采集任务 exception: %s ", Tools.getTrace(r.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                createLog.createLogRecord("任务管理","error","控制任务","查找是否存在启动任务失败");
                return;
            }else if(r.result().size() == 0){//此时没有采集任务
                System.out.println("此时没有启动任务");
                mongoClient.find("task",searchObject,re->{//从任务表中查找相关配置项
                    if(re.failed()){
                        logger.error(String.format("search 采集任务: %s 查找失败", Tools.getTrace(re.cause())));
                        response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                        createLog.createLogRecord("任务管理","error","控制任务","查找采集任务失败");
                        return;
                    }else if(re.result().size() == 0){
                        logger.error("采集任务为不存在");
                        response.error(RECORD_NOT_EXISTED.getCode(), RECORD_NOT_EXISTED.getMsg());
                        createLog.createLogRecord("任务管理","error","控制任务",String.format("采集任务：%s的配置项不存在",id));
                        return;
                    }
                    JsonObject taskResult = re.result().get(0);
                    String targetIP = taskResult.getString("targetIP");
                    JsonObject tdrCfg = taskResult.getJsonObject("tdrCfg");//通信配置项
                    JsonObject anaCfg = taskResult.getJsonObject("anaCfg");//分析配置项

                    if(tdrCfg != null && !tdrCfg.toString().equals("{}")){//如果通信配置项非空，则发送通信配置
                        HdpMsg.EDT_TDR_CFGCTR_MSG.Builder edt_tdr_cfgctr_msg= UDPTaskBuild.communicationTaskParse(tdrCfg);
                        edt_tdr_cfgctr_msg.setCtrCmd(ctr_cmd_st);
                        HdpMsg.EDT_TDR_CFGCTR_MSG edttdrcfgctrmsg = edt_tdr_cfgctr_msg.build();

                        System.out.println("开始发送UDP");
                        UdpPacket.UDP_COMMUNCATE_PACKET.Builder udpPacket = UdpPacket.UDP_COMMUNCATE_PACKET.newBuilder();//构建udp数据包
                        udpPacket.setTimeStamp(System.currentTimeMillis()) //时间戳
                                .setMsgType(1)//消息类型
                                .setIpAddr(targetIP);//目标机ip地址
                        udpPacket.setCurrLength(edttdrcfgctrmsg.toByteString().size())
                                .setTotalLength(edttdrcfgctrmsg.toByteString().size())
                                .setBuffers(edttdrcfgctrmsg.toByteString());

                        JsonObject updateElement = new JsonObject().put("$set",new JsonObject().put("runFlag",runFlag));

                        if(runFlag == 1) {//启动该任务

                            System.out.println("在么有任务的条件下启动一个任务");
                            mongoClient.insert("task", updateElement, rt -> {
                                if (rt.failed()) {
                                    logger.error(String.format("new targetExecuting insert exception: %s", Tools.getTrace(rt.cause())));
                                    response.error(INSERT_FAILURE.getCode(), INSERT_FAILURE.getMsg());
                                    createLog.createLogRecord("任务管理","error","控制任务","启动任务运行标识更新失败");
                                    return;
                                }
                                commandSendsocket.send(Buffer.buffer(udpPacket.build().toByteArray()), port,host, asyncResult -> {
                                    if(asyncResult.failed()){
                                        response.error(COMMUNICATION_UDP_FAILURE.getCode(),COMMUNICATION_UDP_FAILURE.getMsg());
                                        logger.error(String.format("send communication UDP: %s failure",id));
                                        createLog.createLogRecord("任务管理","error","控制任务",String.format("采集任务：%s 通信配置UDP发送失败",id));
                                        return;
                                    }
                                    logger.info(String.format("send communication UDP: %s success",id));
                                    createLog.createLogRecord("任务管理","","控制任务",String.format("采集任务：%s 通信配置UDP发送失败成功",id));
                                    futureCom.complete();
                                });
                            });
                        }else {//停止该任务
                            System.out.println("在么有任务的条件下停止任务");
                            logger.error("此时没有采集任务正在运行");
                            response.error(TASKNOEXECUTING.getCode(),TASKNOEXECUTING.getMsg());
                            createLog.createLogRecord("任务管理","error","控制任务","没有采集任务正在执行");
                            return;
                        }

                    }else {
                        logger.error(String.format("ctr task exception: %s",String.format("需要启动的采集任务：%s的通信配置为空",id)));
                        createLog.createLogRecord("任务管理","error","控制任务",String.format("需要启动的采集任务：%s的通信配置为空",id));
                        response.error(CFGCTRIDTDRCFG.getCode(), CFGCTRIDTDRCFG.getMsg());
                        return;
                    }
                    //若分析配置非空，发送分析配置
                    futureCom.setHandler(ra->{
                        if(anaCfg != null && !anaCfg.toString().equals("{}")){
                            AnalysisCfg.EDT_DATA_ANALYSIS_CFG.Builder edt_data_analysis_cfg = UDPTaskBuild.analysisTaskPaese(anaCfg);
                            edt_data_analysis_cfg.setTargetGuid(id);//设置采集任务标识
                            AnalysisCfg.EDT_DATA_ANALYSIS_CFG edtDataAnalysisCfg = edt_data_analysis_cfg.build();
                            UdpPacket.UDP_COMMUNCATE_PACKET.Builder udpPacket = UdpPacket.UDP_COMMUNCATE_PACKET.newBuilder();//构建udp数据包
                            udpPacket.setTimeStamp(System.currentTimeMillis()) //时间戳
                                    .setMsgType(3)//分析配置
                                    .setIpAddr(targetIP);//ip地址
                            udpPacket.setCurrLength(edtDataAnalysisCfg.toByteString().size())
                                    .setTotalLength(edtDataAnalysisCfg.toByteString().size())
                                    .setBuffers(edtDataAnalysisCfg.toByteString());
                            commandSendsocket.send(Buffer.buffer(udpPacket.build().toByteArray()),port,host,asyncResult->{
                                if(asyncResult.failed()){
                                    response.error(ANALYSIS_UDP_FAILURE.getCode(),ANALYSIS_UDP_FAILURE.getMsg());
                                    logger.error(String.format("send analysis UDP: %s failure",id));
                                    createLog.createLogRecord("任务管理","error","控制任务",String.format("采集任务：%s 分析配置UDP发送失败",id));
                                    return;
                                }
                                logger.info(String.format("send analysis UDP: %s success",id));
                                createLog.createLogRecord("任务管理","info","控制任务",String.format("采集任务：%s 分析配置UDP发送成功",id));
                                futureAna.complete();
                            });
                        }else {
                            futureAna.complete();
                        }

                    });




                });

            }else if(r.result().size() == 1){//存在一个采集任务

                String taskIDIsExecuting = r.result().get(0).getString("id");//正在执行采集任务的taskID
                System.out.println("存在正在采集的任务");
                if(runFlag == 1){
                    System.out.println("启动一个采集任务");
                    logger.error(String.format("采集任务：%s 正在运行",taskIDIsExecuting));
                    response.error(TASKISEXECUTING.getCode(), TASKISEXECUTING.getMsg());
                    createLog.createLogRecord("任务管理","error","控制任务",String.format("采集任务：%s 正在执行，无法启动新任务",id));
                    return;
                }else {//存在一个采集任务，且需要停止
                    System.out.println("停止采集任务");
                    if(taskIDIsExecuting.equals(id)){//正在执行的任务需要停止
                        JsonObject updateElement = new JsonObject().put("runFlag",runFlag);
                        JsonObject updateObject = new JsonObject().put("$set",updateElement);
                        mongoClient.findOneAndUpdate("task",new JsonObject().put("id",id),updateObject,rs->{
                            if(rs.failed()){
                                logger.error(String.format("update taskExecuting exception: %s", Tools.getTrace(rs.cause())));
                                response.error(UPDATE_FAILURE.getCode(), UPDATE_FAILURE.getMsg());
                                createLog.createLogRecord("任务管理","error","控制任务",String.format("采集任务：%s 关闭状态更新失败",id));
                                return;
                            }
                            mongoClient.find("task",searchObject,re->{
                                if(re.failed()){
                                    logger.error(String.format("search 采集任务: %s 查找失败", Tools.getTrace(re.cause())));
                                    response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                                    createLog.createLogRecord("任务管理","error","控制任务","查找采集任务失败");
                                    return;
                                }else if(re.result().size() == 0){
                                    logger.error("采集任务为不存在");
                                    response.error(RECORD_NOT_EXISTED.getCode(), RECORD_NOT_EXISTED.getMsg());
                                    createLog.createLogRecord("任务管理","error","控制任务",String.format("采集任务：%s不存在",id));
                                    return;
                                }

                                JsonObject taskResult = re.result().get(0);
                                String targetIP = taskResult.getString("targetIP");
                                JsonObject tdrCfg = taskResult.getJsonObject("tdrCfg");
                                if(tdrCfg == null || tdrCfg.toString().equals("{}")){
                                    logger.error(String.format("ctr exception: %s",String.format("采集任务：%s的通信配置不存在",id)));
                                    createLog.createLogRecord("任务管理","error","控制任务",String.format("采集任务：%s的通信配置不存在",id));
                                    response.error(CFGCTRIDTDRCFG.getCode(), CFGCTRIDTDRCFG.getMsg());
                                    return;
                                }
                                System.out.println("开始发送停止的udp");
                                HdpMsg.EDT_TDR_CFGCTR_MSG.Builder edt_tdr_cfgctr_msg= UDPTaskBuild.communicationTaskParse(tdrCfg);
                                edt_tdr_cfgctr_msg.setCtrCmd(ctr_cmd_st);
                                HdpMsg.EDT_TDR_CFGCTR_MSG edttdrcfgctrmsg = edt_tdr_cfgctr_msg.build();

                                UdpPacket.UDP_COMMUNCATE_PACKET.Builder udpPacket = UdpPacket.UDP_COMMUNCATE_PACKET.newBuilder();//构建udp数据包
                                udpPacket.setTimeStamp(System.currentTimeMillis()) //时间戳
                                        .setMsgType(1)//消息类型
                                        .setIpAddr(targetIP);//ip地址
                                udpPacket.setCurrLength(edttdrcfgctrmsg.toByteString().size())
                                        .setTotalLength(edttdrcfgctrmsg.toByteString().size())
                                        .setBuffers(edttdrcfgctrmsg.toByteString());
                                commandSendsocket.send(Buffer.buffer(udpPacket.build().toByteArray()),port,targetIP,asyncResult->{
                                    if(asyncResult.failed()){
                                        response.error(COMMUNICATION_UDP_FAILURE.getCode(),COMMUNICATION_UDP_FAILURE.getMsg());
                                        logger.error(String.format("send communication UDP: %s failure",Tools.getTrace(asyncResult.cause())));
                                        createLog.createLogRecord("任务管理","error","控制任务",String.format("采集任务：%s 分析配置UDP发送失败",id));
                                        return;
                                    }
                                    logger.info(String.format("采集任务：%s 停止成功",id));
                                    response.success(new JsonObject());
                                    createLog.createLogRecord("任务管理","info","控制任务",String.format("采集任务：%s 分析配置UDP发送成功",id));
                                    return;
                                });

                            });
                        });
                    }else {//
                        logger.info(String.format("采集任务：%s 处于停止状态",id));
                        response.error(TASKISSTIOP.getCode(), TASKISSTIOP.getMsg());
                        return;
                    }

                }
            }

            futureAna.setHandler(re->{//启动成功后，周期查询玖翼mySQL
                timerID = vertx.setPeriodic(2000,rt->{
                    mySQLClient.query(query,rm->{
                        if(rm.failed()){
                            logger.error(String.format("search communicationData: %s 查找失败", Tools.getTrace(rm.cause())));
                            response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                            return;
                        }else if(rm.result().getRows().size() != 0){
                            String drt_id = rm.result().getRows().get(0).getString("drt_id");//当前启动任务对应的drt_id
                            mongoClient.find("task",new JsonObject().put("drt_id",drt_id),rq->{
                                if(rq.failed()){
                                    logger.error(String.format("search task drt_id: %s 查找失败", Tools.getTrace(rq.cause())));
                                    response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                                    return;
                                }else if(rq.result().size() == 0){
                                    JsonObject updateElement = new JsonObject().put("drt_id",drt_id);
                                    JsonObject updateObject = new JsonObject().put("$set",updateElement);
                                    mongoClient.findOneAndUpdate("task",new JsonObject().put("id",id),updateObject,ru->{
                                        if(ru.failed()){
                                            logger.error(String.format("Update task exception: %s", Tools.getTrace(ru.cause())));
                                            response.error(UPDATE_FAILURE.getCode(), UPDATE_FAILURE.getMsg());
                                            return;
                                        }
                                        if(timerID != Long.MIN_VALUE){
                                            vertx.cancelTimer(timerID);//取消周期性计时器
                                        }
                                    });
                                }
                            });
                        }
                    });
                });
            });
        });

    }
}
