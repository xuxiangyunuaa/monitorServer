//package org.nit.monitorserver.handler.targetMachine;
//import io.vertx.core.Vertx;
//import io.vertx.core.json.JsonObject;
//import org.nit.monitorserver.constant.GlobalConsts;
//import io.vertx.ext.sql.SQLClient;
//import io.vertx.ext.web.RoutingContext;
//import org.apache.log4j.Logger;
//import org.nit.monitorserver.constant.HttpHeaderContentType;
//import org.nit.monitorserver.database.MysqlConnection;
//import org.nit.monitorserver.message.AbstractRequestHandler;
//import org.nit.monitorserver.message.Request;
//import org.nit.monitorserver.message.ResponseFactory;
//import org.nit.monitorserver.udpClient.udpClient;
//
//import static org.nit.monitorserver.constant.ResponseError.*;
///**
// * 功能描述: <这个是运行指令集也没用了>
// * 〈〉
// * @Author: 20643
// * @Date: 2020-9-1 14:23
// */
//public class commandRunInfo extends AbstractRequestHandler {
//
//    protected static final Logger logger = Logger.getLogger(commandRunInfo.class);
//    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
//    private final udpClient udpclient = new udpClient();
//    private final Vertx vertx = Vertx.vertx();
//
//    @Override
//    public void handle(final RoutingContext routingContext,final Request request) {
//
//        //System.out.println("00000000");
//
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
//        ResponseFactory response = new ResponseFactory(routingContext, request);
//
//
//        String cmd = request.getParams().getString(GlobalConsts.COMMAND_SET_CONTENT); //获取指令集
//
//        // 验证非空
//        if (cmd.equals("")) {
//            logger.error(String.format("insert exception: %s", "指令集名称为必填参数"));
//            response.error(COMMANDCONTENT_IS_REQUIRED.getCode(), COMMANDCONTENT_IS_REQUIRED.getMsg());
//            return;
//        }
//
//        JsonObject result = new JsonObject();
//        result.put("data", "命令执行成功！");
//        response.success(result);
//        return;
//
//
////        //命令发送
////        int port=IniUtil.getInstance().getUdpServerPort();
////        String host=IniUtil.getInstance().getUdpServerHost();
////
////
////        udpclient.setPort(port);
////        udpclient.setAddress(host);
////        udpclient.setBuffer(cmd.getBytes());
////        if(udpclient.send().equals("send failed!")){
////            logger.error(String.format("命令:%s发送失败！",cmd));
////            response.error(COMMAND_SEND_ERROR.getCode(), COMMAND_SEND_ERROR.getMsg());
////        }
//
//        //监听反馈消息
////        DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());
////        int commandPort=IniUtil.getInstance().getCommandPort();
////        String commandHost=IniUtil.getInstance().getRedisHost();
////
////        Future receive=Future.future();
////        socket.listen(commandPort,commandHost,asyncResult->{
////            if(asyncResult.succeeded()){
////                socket.handler(packet->{
////                    receive.complete(packet.data().getBytes());
//////                    byte[] result=packet.data().getBytes();
////
////                });
////            }
////        });
////        receive.setHandler(rece->{
////
////        });
//
//
//
//
//
//
//    }
//
//}
