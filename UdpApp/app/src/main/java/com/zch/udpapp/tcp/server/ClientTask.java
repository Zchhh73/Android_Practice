package com.zch.udpapp.tcp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ClientTask extends Thread implements MsgPool.MsgComingListener {

    //读取客户端发的信息，发送的信息
    private Socket mSocket;
    private InputStream mIs;
    private OutputStream mOs;

    public ClientTask(Socket socket) {
        try {
            mSocket=socket;
            mIs=socket.getInputStream();
            mOs=socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //客户端写数据，通过流读到
        BufferedReader br=new BufferedReader(new InputStreamReader(mIs));
        String line=null;
        try {
            while ((line=br.readLine()) != null) {
                //读数据
                System.out.println("read:" + line);
                //转发消息至其他socket
                MsgPool.getInstance().sendMsg(mSocket.getPort() + ":" + line);

            }
        } catch (Exception e) {

        }


    }

    @Override
    public void onMsgComing(String msg) {
        //返回数据,通过output流写数据
        try {
            mOs.write(msg.getBytes());
            mOs.write("\n".getBytes());
            mOs.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
