package org.nit.monitorserver.udpClient;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;

public class udpClient {
    public int port;
    public String address;
    public int listenPort;
    public String listenAddress;
    public byte[] buffer;
    private final Vertx vertx = Vertx.vertx();

    public void setPort(int port) {
        this.port=port;
    }

    public void setListenPort(int listenPort) {
        this.listenPort=listenPort;
    }

    public void setAddress(String address) {
        this.address=address;
    }

    public void setListenAddress(String listenAddress) {
        this.listenAddress=listenAddress;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer=buffer;
    }

    public String send(){
        DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());

        socket.send(Buffer.buffer(buffer),port,address,asyncResult->{
            if(asyncResult.succeeded()){
                return;
            }
        });

        return "send failed!";
    }

//    public byte[] ListenAcquisition(){
//        DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());
//
//        Future receive=Future.future();
//        socket.ListenAcquisition(listenPort,listenAddress,asyncResult->{
//            if(asyncResult.succeeded()){
//                socket.handler(packet->{
//                    receive.complete(packet.data().getBytes());
////                    byte[] result=packet.data().getBytes();
//                    return;
//                });
//            }
//        });
//        receive.setHandler(rece->{
//            byte[]
//        });
//        return "Faild!".getBytes();
//    }

}
