package com.zch.udpapp.biz;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UdpClientBiz {

    private String mServerIp="192.168.246.2";
    private int mServerPort=7777;
    private InetAddress mServerAddress;
    private DatagramSocket mSocket;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public UdpClientBiz() {
        try {
            mServerAddress=InetAddress.getByName(mServerIp);
            mSocket=new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface onMsgReturnedListener {
        void onMsgReturned(String msg);

        void onError(Exception e);
    }


    public void sendMsg(final String msg, final onMsgReturnedListener listener) {

        new Thread() {
            @Override
            public void run() {
                try {
                    //send
                    byte[] clientMsgBytes=msg.getBytes();
                    DatagramPacket clientPacket=new DatagramPacket(clientMsgBytes,
                            clientMsgBytes.length, mServerAddress, mServerPort);
                    mSocket.send(clientPacket);

                    //receiver
                    byte[] buf=new byte[1024];
                    DatagramPacket serverMsgPacket=new DatagramPacket(buf, buf.length);
                    mSocket.receive(serverMsgPacket);
                    //得到消息，解析
                    InetAddress address=serverMsgPacket.getAddress();
                    int port=serverMsgPacket.getPort();
                    final String serverMsg=new String(serverMsgPacket.getData(),
                            0, serverMsgPacket.getLength());

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onMsgReturned(serverMsg);
                        }
                    });
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError(e);
                        }
                    });
                }
            }
        }.start();
    }

    public void onDestroy(){
        if(mSocket != null){
            mSocket.close();
        }
    }


}
