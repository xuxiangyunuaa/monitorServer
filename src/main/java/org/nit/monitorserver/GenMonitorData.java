package org.nit.monitorserver;

import com.google.protobuf.InvalidProtocolBufferException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.SQLClient;
import org.apache.log4j.Logger;
import org.nit.monitorserver.database.MysqlConnection;
import org.nit.monitorserver.proto.TdrMsg;
import org.nit.monitorserver.proto.UdpPacket;
import org.nit.monitorserver.util.IniUtil;
import org.nit.monitorserver.util.Tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

//import org.nit.monitorserver.database.MongoConnection;

/**
 * @ClassName GenMonitorData
 * @Description TODO生成模拟数据
 * @Author 20643
 * @Date 2020-9-1 20:24
 * @Version 1.0
 **/
public class GenMonitorData extends AbstractVerticle {
    protected static final Logger logger = Logger.getLogger(GenMonitorData.class);
    private int port = IniUtil.getInstance().getDacmdPort();
    private String host = IniUtil.getInstance().getServerHostName();
    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
//    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
//    public  String tablename="detrcd_raws_table";
    private final Vertx vertx=Vertx.vertx();
    public GenMonitorData() {

    }
    /**
     * 功能描述: <将数据插入数据库>
     * 〈〉
     * @Author: 20643
     * @Date: 2020-9-1 20:31
     */
    public void insertMysql(String sql,JsonArray params){
        mySQLClient.queryWithParams(sql,params,res->{
            if(res.failed()){
                logger.error(String.format("New data insert exception: %s", Tools.getTrace(res.cause())));
            }
        });
    }


    /**
     * 功能描述: <随机生成数据
     * 〈〉
     * @Author: 20643
     * @Date: 2020-9-1 20:31
     */

    public void genRanData(String uuid){
        String sql="INSERT INTO detrcd_raws_table VALUES(?,?,?,?,?,?)";
        Random r=new Random();
        int type=r.nextInt(4);
        switch (type){
            case 0:
                JsonArray params=new JsonArray().add(uuid).add(10032).add("0").add("0").add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").format(new Date()));
                TdrMsg.EDT_MEMINFOGET_INFO.Builder edt_meminfoget_info= TdrMsg.EDT_MEMINFOGET_INFO.newBuilder();
                TdrMsg.EDT_MEMINFOGET_T.Builder edt_mem_t= TdrMsg.EDT_MEMINFOGET_T.newBuilder();
                edt_mem_t.setFreeBytes(r.nextInt(100000)).setAllocBytes(r.nextInt(10000)).setInternalBytes(r.nextInt(10000))
                        .setCumBytesAllocated(r.nextInt(10000)).setFreeBlocks(r.nextInt(1000)).setAllocBlocks(r.nextInt(1000))
                        .setInternalBlocks(r.nextInt(1000)).setCumBlocksAllocated(r.nextInt(1000)).setFreeAvgBlock(r.nextInt(10000))
                        .setAllocAvgBlock(r.nextInt(10000)).setInternalAvgBlock(r.nextInt(10000)).setCumAvgBlock(r.nextInt(10000))
                        .setFreeMaxBlock(r.nextInt(10000)).setAllocMaxBytes(r.nextInt(10000));
                edt_meminfoget_info.setEvtId(10032).setTimeStamp(System.currentTimeMillis()).setPdId(0).setPortId(50000).setMemInfoGet(edt_mem_t);
                params.add(edt_meminfoget_info.build().toByteArray());
                System.out.println(edt_meminfoget_info);
//                System.out.println(edt_meminfoget_info.build());
//                System.out.println(edt_meminfoget_info.build().toByteArray());
                insertMysql(sql,params);
            case 1:
                JsonArray params1=new JsonArray().add(uuid).add(40010).add("0").add("1").add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").format(new Date()));
                TdrMsg.EDT_OSACSEND_INFO.Builder osac_send= TdrMsg.EDT_OSACSEND_INFO.newBuilder();
                osac_send.setEvtId(40010).setTimeStamp(System.currentTimeMillis()).setPdId(0).setPortId(2).setOsacId(50000).setPortType(11).setTimeOut(1000)
                        .setName("osac_send");
                for(int i=0;i<128;i++){
                    osac_send.addMessage(r.nextInt(2));
                }
                osac_send.setLength(osac_send.getMessageCount());
                params1.add(osac_send.build().toByteArray());
                System.out.println(osac_send);
//                System.out.println(osac_send.build());
//                System.out.println(osac_send.build().toByteArray());
                insertMysql(sql,params1);
            case 2:
                JsonArray params2=new JsonArray().add(uuid).add(40019).add("0").add("2").add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").format(new Date()));
                TdrMsg.EDT_OSACRECV_INFO.Builder osac_rec= TdrMsg.EDT_OSACRECV_INFO.newBuilder();
                osac_rec.setEvtId(40019).setTimeStamp(System.currentTimeMillis()).setPdId(0).setPortId(2).setOsacId(50000).setPortType(11).setTimeOut(1000)
                        .setName("osac_rec");
                for(int i=0;i<128;i++){
                    osac_rec.addMessage(r.nextInt(2));
                }
                osac_rec.setLength(osac_rec.getMessageCount());
                params2.add(osac_rec.build().toByteArray());
                System.out.println(osac_rec);
//                System.out.println(osac_rec.build());
//                System.out.println(osac_rec.build().toByteArray());
                insertMysql(sql,params2);
            case 3:
                JsonArray params3=new JsonArray().add(uuid).add(10510).add("0").add("0").add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").format(new Date()));
                TdrMsg.EDT_CRE_BLACKBOARD_INFO.Builder edt_blockborad= TdrMsg.EDT_CRE_BLACKBOARD_INFO.newBuilder();
                edt_blockborad.setEvtId(10510).setTimeStamp(System.currentTimeMillis()).setPdId(0).setPortId(50000).setBlackBoardId(100)
                        .setMsgSize(r.nextInt(5000)).setName("Create BlockBoard");
                params3.add(edt_blockborad.build().toByteArray());
                System.out.println(edt_blockborad);
//                System.out.println(edt_blockborad.build());
//                System.out.println(edt_blockborad.build().toByteArray());
                insertMysql(sql,params3);
            default:
                break;
        }
    }
    @Override
    public void start(){
        DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());//创建套接字

        socket.listen(9099,"127.0.0.1",asyncResult->{//监听
            if(asyncResult.succeeded()){
                socket.handler(packet->{
                    try {//通信数据与分析数据
                        UdpPacket.UDP_COMMUNCATE_PACKET udppacket= UdpPacket.UDP_COMMUNCATE_PACKET.parseFrom(packet.data().getBytes());//proto格式
                        int msgtype = udppacket.getMsgType();//消息类型，请求数据为1
                        long timerID=0;
                        switch (msgtype){
                            case 1:
                                String uuid=UUID.randomUUID().toString().replaceAll("-","");
                                timerID=vertx.setPeriodic(30,r->{
                                    genRanData(uuid);
                                });
                            default:
                                break;
                                //无
                        }
                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

//        logger.info(String.format("please visit url: http://%s:%d%s", this.host, this.port, "/"));
    }

}
