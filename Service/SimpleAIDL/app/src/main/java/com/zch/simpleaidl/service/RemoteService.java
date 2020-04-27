package com.zch.simpleaidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.zch.simpleaidl.IRemoteCallback;
import com.zch.simpleaidl.IRemoteService;

public class RemoteService extends Service {
    private static final String TAG="RemoteService";

    private RemoteCallbackList<IRemoteCallback> mCallbacks;
    private Handler handler;

    public RemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        mCallbacks = new RemoteCallbackList<>();
        handler = new Handler();
        push();
        return stub;
    }

    private void push() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                while(true){
                    // 以广播的方式进行客户端回调
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            int len = mCallbacks.beginBroadcast();
                            for(int i = 0;i<len;i++){
                                try {
                                    mCallbacks.getBroadcastItem(i).onSuccess("push","接口回调回来的数据" + System.currentTimeMillis());
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                            mCallbacks.finishBroadcast();
                        }
                    });
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    IRemoteService.Stub stub = new IRemoteService.Stub() {
        @Override
        public void Regitster(IRemoteCallback callback) throws RemoteException {
            if (callback == null){
                return;
            }
            Log.i(TAG, "Service注册回调");
            mCallbacks.register(callback);
        }

        @Override
        public void unRegitster(IRemoteCallback callback) throws RemoteException {
            if(callback==null){
                return;
            }
            Log.i(TAG, "Service反注册回调");
            mCallbacks.unregister(callback);
        }

        @Override
        public void send(String packageName, String func, String params) throws RemoteException {
            Log.i(TAG, "接收到请求step0");
            if(TextUtils.isEmpty(func)){
                return;
            }
            switch (func){
                case "test":
                    Log.i(TAG, "接收到请求step1");
                    break;
            }
        }
    };


}
