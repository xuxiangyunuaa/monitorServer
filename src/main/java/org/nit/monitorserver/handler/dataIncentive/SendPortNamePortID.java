//package org.nit.monitorserver.handler.dataIncentive;
//
//import com.google.protobuf.ByteString;
//import io.vertx.core.AbstractVerticle;
//import io.vertx.core.Future;
//import io.vertx.core.buffer.Buffer;
//import io.vertx.core.datagram.DatagramSocket;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.mongo.MongoClient;
//import org.apache.log4j.Logger;
//import org.nit.monitorserver.database.MongoConnection;
//import org.nit.monitorserver.handler.task.CommunicationCommand;
////import org.nit.monitorserver.proto.UdpPacket;
//import org.nit.monitorserver.proto.UdpPacket;
//import org.nit.monitorserver.util.IniUtil;
//import org.nit.monitorserver.util.Tools;
///**
// * 功能描述: <合并到控制功能里面>
// * 〈〉
// * @Author: 20643
// * @Date: 2020-9-1 15:02
// */
//public class SendPortNamePortID extends AbstractVerticle {
//    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
//    protected static final Logger logger = Logger.getLogger(CommunicationCommand.class);
//    private int port = IniUtil.getInstance().getDacmdPort();
//    private String host = IniUtil.getInstance().getServerHostName();
//    public UdpPacket.UDP_COMMUNCATE_PACKET.Builder createData(int currLength, int totalLength, byte[] buffer){
//        UdpPacket.UDP_COMMUNCATE_PACKET.Builder udpPacket = UdpPacket.UDP_COMMUNCATE_PACKET.newBuilder();//构建udp数据包
//        udpPacket.setTimeStamp(System.currentTimeMillis())
//                .setMsgType(7)//portName+portID
//                .setTotalLength(currLength)
//                .setTotalLength(totalLength)
//                .setBuffers(ByteString.copyFrom(buffer));
//        return udpPacket;
//    }
//
//    public void start(){
//        DatagramSocket commandSendsocket=vertx.createDatagramSocket();;//创建套接字
//        Future future = Future.future();
//        mongoClient.find("portNamePortID",new JsonObject(),r->{
//            if(r.failed()){
//                logger.error(String.format("ICDArray exception: %s", Tools.getTrace(r.cause())));
//                return;
//            }else {
//                byte[] bytes = r.result().get(0).toString().getBytes();
//                int currLength = ByteString.copyFrom(bytes).size();
//                int totalLength = currLength;
//                UdpPacket.UDP_COMMUNCATE_PACKET.Builder packet = createData(currLength,totalLength,bytes);
//                commandSendsocket.send(Buffer.buffer(packet.build().toByteArray()),port,host, re->{//向玖翼发送portName+portID
////                    System.out.println("发送数据");
//                    if(re.failed()){
//                        logger.error(String.format("ICDArray send failed: %s", Tools.getTrace(re.cause())));
//                        return;
//                    }
//
//                });
//
//
//
//
//
//
//
//            }
//
//        });
//
//
//
//    }
//
//
//
//}
