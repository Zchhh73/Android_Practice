package com.zch.udpapp.tcp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class MsgPool {

    private static MsgPool sInstance = new MsgPool();

    private LinkedBlockingDeque<String> mQueue = new LinkedBlockingDeque<>();


    public static MsgPool getInstance(){
        return sInstance;
    }

    private MsgPool(){
    }

    /**
     * 观察者模式
     */
    public interface MsgComingListener{
        void onMsgComing(String msg);
    }

    private List<MsgComingListener> mListeners = new ArrayList<>();

    public void addMsgComingListener(MsgComingListener listener){
        mListeners.add(listener);
    }


    public void sendMsg(String msg){
        try {
            mQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        new Thread(){
            @Override
            public void run() {
                //取数据
                while (true){
                    try {
                        String msg=mQueue.take();
                        notifyMsgComing(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void notifyMsgComing(String msg) {
        //通知消息来了，
        for(MsgComingListener listener:mListeners){
            listener.onMsgComing(msg);
        }
    }

}
