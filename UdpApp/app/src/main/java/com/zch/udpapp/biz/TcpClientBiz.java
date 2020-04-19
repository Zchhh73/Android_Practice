package com.zch.udpapp.biz;

import android.os.Handler;
import android.os.Looper;

import com.zch.udpapp.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TcpClientBiz {
    private Socket mSocket;
    private InputStream mIs;
    private OutputStream mOs;

    private Handler mHandler = new Handler(Looper.getMainLooper());


    public TcpClientBiz() {

        new Thread() {
            @Override
            public void run() {
                try {
                    mSocket=new Socket("192.168.246.2", 9090);
                    mIs=mSocket.getInputStream();
                    mOs=mSocket.getOutputStream();
                    readServerMsg();
                } catch (final IOException e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(mListener != null){
                                mListener.onError(e);
                            }
                        }
                    });
                }
            }
        }.start();
    }

    private void readServerMsg() throws IOException {
        final BufferedReader br=new BufferedReader(new InputStreamReader(mIs));
        //接收显示server发的数据
        String line=null;
        //回调到Activity控件显示,handler
        while ((line=br.readLine()) != null) {
            final String finalLine=line;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mListener != null){
                        mListener.onMsgComing(finalLine);
                    }
                }
            });
        }
    }

    public void sendMsg(final String msg) {

        new Thread() {
            @Override
            public void run() {
                try {
                    BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(mOs));
                    bw.write(msg);
                    bw.newLine();
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }



    public interface onTmsgCommingListner{
        void onMsgComing(String msg);
        void onError(Exception e);
    }

    private onTmsgCommingListner mListener;

    public void setonTmsgCommingListner(onTmsgCommingListner listener){
        mListener = listener;
    }


    public void onDestroy() {

        try {
            if(mIs!=null){
                mIs.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if(mOs!=null){
                mOs.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if(mSocket!=null){
                mSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
