package com.zch.serviceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //ServicerConnection
    //IBinder
    //进度监控


    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 当客户端正常连接服务时，执行服务的绑定操作被调用
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            /*
            MyService.MyBinder mb=(MyService.MyBinder) service;
            int progress=mb.getProgress();
            Log.d(MyService.TAG, "当前进度是: "+progress);
             */
            IMyAidlInterface imai = IMyAidlInterface.Stub.asInterface(service);
            try {
                imai.showProgress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        /**
         * 连接失败时调用
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void operate(View v) {
        switch (v.getId()) {
            case R.id.start:

                //启动服务
                Intent intent1=new Intent(this, MyService.class);
                startService(intent1);
                break;

            case R.id.bind:
                //绑定服务
                Intent intent3 = new Intent(this,MyService.class);
                bindService(intent3,conn,BIND_AUTO_CREATE);

                break;

            case R.id.unbind:
                //解绑
                unbindService(conn);
                break;

            case R.id.stop:
                Intent intent2=new Intent(this, MyService.class);
                stopService(intent2);
                break;
        }
    }
}
