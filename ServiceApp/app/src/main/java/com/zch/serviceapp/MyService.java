package com.zch.serviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.io.FileDescriptor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyService extends Service {
    public static final String TAG="MyService";
    private int i;

    public MyService() {
    }
    /**
     * 创建
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "服务创建");
        //模拟耗时任务
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    for (i=0; i < 100; i++) {

                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 启动
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "服务启动");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 绑定
     * IBinder:用于远程操作对象的接口
     */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "服务绑定了");
//        throw new UnsupportedOperationException("Not yet implemented");
        //Binder
        return new IMyAidlInterface.Stub() {

            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

            }

            @Override
            public void showProgress() throws RemoteException {
                Log.d(TAG, "当前进度是："+i);
            }
        };
    }

    //对于onBind方法，要求返回IBinder对象。实际上，会自己定义一个内部类，继承Binder类。
    class MyBinder extends Binder {
        //实现进度监控
        public int getProgress(){
            return i;
        }

    }

    /**
     * 解绑
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "服务解绑了");
        return super.onUnbind(intent);
    }

    /**
     * 摧毁
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "服务销毁了");
    }
}
