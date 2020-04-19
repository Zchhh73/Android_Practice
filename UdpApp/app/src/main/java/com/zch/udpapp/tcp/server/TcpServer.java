package com.zch.udpapp.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

    public void start(){
        //1.启动服务器套接字
        ServerSocket sSocket = null;
        try {
            sSocket = new ServerSocket(9090);

            //启动消息队列，不断从消息队列中取数据
            MsgPool.getInstance().start();

            while(true){
                //客户端套接字，等待客户端程序到来
                Socket s=sSocket.accept();
                System.out.println("ip:"+s.getInetAddress().getHostAddress()+
                        ",port:"+s.getPort()+" is online...");


                //启动ClientTask，监听MsgComingListener
                ClientTask task = new ClientTask(s);
                MsgPool.getInstance().addMsgComingListener(task);
                task.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TcpServer().start();
    }
}
