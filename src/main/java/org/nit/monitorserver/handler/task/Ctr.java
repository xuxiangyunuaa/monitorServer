package org.nit.monitorserver.handler.task;

import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.proto.AnalysisCfg;
import org.nit.monitorserver.proto.HdpMsg;
import org.nit.monitorserver.proto.UdpPacket;
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.IniUtil;
import org.nit.monitorserver.util.Tools;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @ClassName CtrDataIncentive
 * @Description TODO控制采集任务
 * @Author 20643
 * @Date 2020-9-1 14:38
 * @Version 1.0
 **/
public class Ctr extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(Ctr.class);
    //    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    private Vertx vertx = Vertx.vertx();
    private int port = IniUtil.getInstance().getUdpServerPort();
    private String host = IniUtil.getInstance().getUdpServerHost();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);


        HdpMsg.CTR_CMD_ST.Builder ctr_cmd_st = HdpMsg.CTR_CMD_ST.newBuilder();

        String id = request.getParams().getString("id");
        Object cfgCtrIdObject = request.getParams().getValue("cfgCtrId");
        Object runFlagObject = request.getParams().getValue("runFlag");
        //id
        if(id.equals("") || id == null){
            logger.error(String.format("ctr exception: %s", "采集任务id为必填参数"));
            response.error(TASKID.getCode(), TASKID.getMsg());
            return;
        }
        JsonObject searchObject = new JsonObject().put("id",id);
        int cfgCtrId = -1;
        if(cfgCtrIdObject == null){
            logger.error(String.format("ctr exception: %s", "配置控制事件id为必填参数"));
            response.error(CFGCTRID.getCode(), CFGCTRID.getMsg());
            return;
        }
        if(!FormValidator.isInteger(cfgCtrIdObject)){
            logger.error(String.format("ctr exception: %s", "配置控制事件id格式错误"));
            response.error(CFGCTRID_FORMAT_ERROR.getCode(), CFGCTRID_FORMAT_ERROR.getMsg());
            return;
        }
        cfgCtrId = (int) cfgCtrIdObject;
        ctr_cmd_st.setCfgCtrId(cfgCtrId);

        if(runFlagObject == null){
            logger.error(String.format("ctr exception: %s", "运行标识为必填参数"));
            response.error(RUNFLAG.getCode(), RUNFLAG.getMsg());
            return;
        }
        if(!FormValidator.isInteger(runFlagObject)){
            logger.error(String.format("ctr exception: %s", "运行标识格式错误"));
            response.error(RUNFLAG_FORMAT_ERROR.getCode(), RUNFLAG_FORMAT_ERROR.getMsg());
            return;
        }
        int runFlag = (int) runFlagObject;
        if(runFlag != 0 && runFlag != 1){
            logger.error(String.format("ctr exception: %s", "运行标识格式错误"));
            response.error(RUNFLAG_FORMAT_ERROR.getCode(), RUNFLAG_FORMAT_ERROR.getMsg());
            return;
        }
        ctr_cmd_st.setRunFlag(runFlag);
        System.out.println("运行标识为："+runFlag);

        Object apexFlagObject = request.getParams().getValue("apexFlag");
        if(apexFlagObject != null){
            if(!FormValidator.isInteger(apexFlagObject)){
                logger.error(String.format("ctr exception: %s", "分区apex信息采集格式错误"));
                response.error(APEXFLAG_FORMAT_ERROR.getCode(), APEXFLAG_FORMAT_ERROR.getMsg());
                return;
            }
            int apexFlag = (int) apexFlagObject;
            if(apexFlag != 0 && apexFlag != 1){
                logger.error(String.format("ctr exception: %s", "分区apex信息采集格式错误"));
                response.error(APEXFLAG_FORMAT_ERROR.getCode(), APEXFLAG_FORMAT_ERROR.getMsg());
                return;
            }
            ctr_cmd_st.setApexFlag(apexFlag);

        }


        Object posixFlagObject = request.getParams().getValue("posixFlag");
        if(posixFlagObject != null){
            if(!FormValidator.isInteger(posixFlagObject)){
                logger.error(String.format("ctr exception: %s", "分区posix信息采集格式错误"));
                response.error(POSIXFLAG_FORMAT_ERROR.getCode(), POSIXFLAG_FORMAT_ERROR.getMsg());
                return;
            }
            int posixFlag = (int) posixFlagObject;
            if(posixFlag != 0 && posixFlag != 1){
                logger.error(String.format("ctr exception: %s", "分区posix信息采集格式错误"));
                response.error(POSIXFLAG_FORMAT_ERROR.getCode(), POSIXFLAG_FORMAT_ERROR.getMsg());
                return;
            }
            ctr_cmd_st.setPosixFlag(posixFlag);

        }

        int anaFlag = -1;
        Object anaFlagObject = request.getParams().getValue("anaFlag");
        if(anaFlagObject != null){
            if(!FormValidator.isInteger(anaFlagObject)){
                logger.error(String.format("ctr exception: %s", "玖翼部分的分析数据格式错误"));
                response.error(ANAFLAG_FORMAT_ERROR.getCode(), ANAFLAG_FORMAT_ERROR.getMsg());
                return;
            }
            anaFlag = (int) anaFlagObject;
            if(anaFlag != 0 && anaFlag != 1){
                logger.error(String.format("ctr exception: %s", "玖翼部分的分析数据格式错误"));
                response.error(ANAFLAG_FORMAT_ERROR.getCode(), ANAFLAG_FORMAT_ERROR.getMsg());
                return;
            }
        }
        System.out.println("参数验证合格");
       //检查是否存在其他启动任务
        mongoClient.find("targetExecuting",new JsonObject(),r->{
            System.out.println("开始查找是否存在启动任务");
            if(r.failed()){
                logger.error(String.format("search 正在运行中的采集任务: %s 查找失败", Tools.getTrace(r.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                return;
            }else if(r.result().size() == 0){//此时没有采集任务
                System.out.println("此时没有启动任务");
                mongoClient.find("task",searchObject,re->{
                    if(re.failed()){
                        logger.error(String.format("search 采集任务: %s 查找失败", Tools.getTrace(re.cause())));
                        response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                        return;
                    }else if(re.result().size() == 0){
                        logger.error("采集任务为不存在");
                        response.error(RECORD_NOT_EXISTED.getCode(), RECORD_NOT_EXISTED.getMsg());
                        return;
                    }
                    JsonObject taskResult = re.result().get(0);
                    String targetIP = taskResult.getString("targetIP");
                    JsonObject tdrCfg = taskResult.getJsonObject("tdrCfg");
                    JsonObject anaCfg = taskResult.getJsonObject("anaCfg");
                    if(tdrCfg == null){
                        logger.error(String.format("ctr exception: %s", "配置控制事件ID与通信配置不匹配"));
                        response.error(CFGCTRIDTDRCFG.getCode(), CFGCTRIDTDRCFG.getMsg());
                        return;
                    }
                    DatagramSocket commandSendsocket = vertx.createDatagramSocket();
                    HdpMsg.EDT_TDR_CFGCTR_MSG.Builder edt_tdr_cfgctr_msg= UDPTaskBuild.communicationTaskParse(tdrCfg);
                    edt_tdr_cfgctr_msg.setCtrCmd(ctr_cmd_st);
                    HdpMsg.EDT_TDR_CFGCTR_MSG edttdrcfgctrmsg = edt_tdr_cfgctr_msg.build();

                    System.out.println("开始发送UDP");
                    UdpPacket.UDP_COMMUNCATE_PACKET.Builder udpPacket = UdpPacket.UDP_COMMUNCATE_PACKET.newBuilder();//构建udp数据包
                    udpPacket.setTimeStamp(System.currentTimeMillis()) //时间戳
                            .setMsgType(1)//消息类型
                            .setIpAddr(targetIP);//ip地址
                    udpPacket.setCurrLength(edttdrcfgctrmsg.toByteString().size())
                            .setTotalLength(edttdrcfgctrmsg.toByteString().size())
                            .setBuffers(edttdrcfgctrmsg.toByteString());

                    String ctrID = Tools.generateId();
                    JsonObject replaceElement = new JsonObject().put("targetIP",targetIP).put("taskID",id).put("ctrID",ctrID);
                    if(runFlag == 1) {//启动该任务
                        System.out.println("在么有任务的条件下启动一个任务");
                        mongoClient.insert("targetExecuting", replaceElement, rt -> {
                            if (rt.failed()) {
                                logger.error(String.format("new targetExecuting insert exception: %s", Tools.getTrace(rt.cause())));
                                response.error(INSERT_FAILURE.getCode(), INSERT_FAILURE.getMsg());
                                return;
                            }
                            commandSendsocket.send(Buffer.buffer(udpPacket.build().toByteArray()), port, host, asyncResult -> {
                                if(asyncResult.failed()){
                                    response.error(COMMUNICATION_UDP_FAILURE.getCode(),COMMUNICATION_UDP_FAILURE.getMsg());
                                    logger.error(String.format("send communication UDP: %s failure",id));
                                    return;
                                }
                                logger.info(String.format("send analysis UDP: %s success",id));
                                response.success(new JsonObject());
                                return;
                            });
                        });
                    }else {
                        System.out.println("在么有任务的条件下停止任务");
                        logger.error("此时没有采集任务正在运行");
                        response.error(TASKNOEXECUTING.getCode(),TASKNOEXECUTING.getMsg());
                        return;
                    }


                });

            }else if(r.result().size() == 1){//存在一个采集任务

                String taskIDIsExecuting = r.result().get(0).getString("taskID");
                System.out.println("存在正在采集的任务");
                if(runFlag == 1){
                    System.out.println("启动一个采集任务");
                    System.out.println("");
                    logger.error(String.format("采集任务：%s 正在运行",id));
                    response.error(TASKISEXECUTING.getCode(), TASKISEXECUTING.getMsg());
                    return;
                }else {//存在一个采集任务，且需要停止
                    System.out.println("停止采集任务");
                    if(taskIDIsExecuting.equals(id)){//正在执行的任务需要停止
                        mongoClient.removeDocuments("targetExecuting",new JsonObject().put("taskID",id),rs->{
                            if(rs.failed()){
                                logger.error(String.format("delete targetExecuting exception: %s", Tools.getTrace(rs.cause())));
                                response.error(DELETE_FAILURE.getCode(), DELETE_FAILURE.getMsg());
                                return;
                            }
                            mongoClient.find("task",searchObject,re->{
                                if(re.failed()){
                                    logger.error(String.format("search 采集任务: %s 查找失败", Tools.getTrace(re.cause())));
                                    response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                                    return;
                                }else if(re.result().size() == 0){
                                    logger.error("采集任务为不存在");
                                    response.error(RECORD_NOT_EXISTED.getCode(), RECORD_NOT_EXISTED.getMsg());
                                    return;
                                }

                                JsonObject taskResult = re.result().get(0);
                                String targetIP = taskResult.getString("targetIP");
                                JsonObject tdrCfg = taskResult.getJsonObject("tdrCfg");
                                JsonObject anaCfg = taskResult.getJsonObject("anaCfg");
                                if(tdrCfg == null){
                                    logger.error(String.format("ctr exception: %s", "配置控制事件ID与通信配置不匹配"));
                                    response.error(CFGCTRIDTDRCFG.getCode(), CFGCTRIDTDRCFG.getMsg());
                                    return;
                                }
                                System.out.println("开始发送停止的udp");
                                DatagramSocket commandSendsocket = vertx.createDatagramSocket();
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
                                        return;
                                    }
                                    logger.info(String.format("采集任务：%s 停止成功",id));
                                    response.success(new JsonObject());
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
        });
        //查看当前采集任务
    }
}
