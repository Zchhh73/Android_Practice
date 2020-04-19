package com.zch.udpapp.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class udpClient {

    private String mServerIp = "192.168.246.2";
    private int mServerPort = 7777;
    private InetAddress mServerAddress;
    private DatagramSocket mSocket;

    private Scanner in;

    public udpClient() {
        try {
            mServerAddress = InetAddress.getByName(mServerIp);
            mSocket = new DatagramSocket();
            in = new Scanner(System.in);
            in.useDelimiter("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(){
        while(true){
            try {
                //发送消息
                String clientMsg=in.next();
                byte[] clientMsgBytes=clientMsg.getBytes();
                DatagramPacket clientPacket = new DatagramPacket(clientMsgBytes,
                        clientMsgBytes.length,mServerAddress,mServerPort);
                mSocket.send(clientPacket);

                //接收数据
                byte[] buf = new byte[1024];
                DatagramPacket serverMsgPacket = new DatagramPacket(buf,buf.length);
                mSocket.receive(serverMsgPacket);
                //得到消息，解析
                InetAddress address = serverMsgPacket.getAddress();
                int port = serverMsgPacket.getPort();
                String serverMsg = new String(serverMsgPacket.getData(),
                        0,serverMsgPacket.getLength());
                System.out.println("message:" + serverMsg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new udpClient().start();
    }
}
