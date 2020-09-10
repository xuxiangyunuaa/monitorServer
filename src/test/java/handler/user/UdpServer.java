package handler.user;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
//import org.nit.monitorserver.binaryRequest.BinaryRequestFactory;
import org.apache.log4j.Logger;
import org.nit.monitorserver.util.Tools;

import java.io.IOException;

/**
 * Created by sensordb on 2019/8/8.
 */
public class UdpServer {
    //private static String host = "39.100.142.148";
    protected static final Logger logger = Logger.getLogger(UdpLoginTest.class);
    private static String host = "127.0.0.1";
    private static int port = 9999;

    public static void main(String[] args) throws Exception {
        Vertx vertx = Vertx.vertx();
        DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());

//        Buffer buffer = BinaryRequestFactory.login("000036", 1, "000000000000003", "0123456789");
        Buffer buffer=Buffer.buffer("test");
        //Buffer buffer = BinaryRequestFactory.login("000001", 1, "000000000000001", "0000000000");
        // Send a Buffer
        socket.listen(9999, "127.0.0.1", asyncResultRe -> {
            if (asyncResultRe.failed()) {
                logger.info(String.format("start udp server:%s port:%d failed: %s",
                        host,
                        port,
                        Tools.getTrace(asyncResultRe.cause())));
                return;
            }
            logger.info(String.format("udp server:%s is now listening at port:%d", host, port));
            socket.send(buffer, port, host, asyncResult -> {
                System.out.println("Send succeeded? " + asyncResult.succeeded());
                //DatagramSocket listenSocket = vertx.createDatagramSocket(new DatagramSocketOptions());
            });

            socket.handler(packet -> {
                // Do something with the packet
                System.out.println(String.format("udp server receive from (%s)",
                        packet.sender()));
            });

        });


    }

}
