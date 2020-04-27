package com.uestc.aidlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import com.zch.serviceapp.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IMyAidlInterface imai = IMyAidlInterface.Stub.asInterface(service);
            try {
                imai.showProgress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

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
                //远程启动服务
                Intent it = new Intent();
                it.setAction("com.zch.myservice");
                it.setPackage("com.zch.serviceapp");
                startService(it);
                break;

            case R.id.bind:
                //远程绑定服务
                Intent it2 = new Intent();
                it2.setAction("com.zch.myservice");
                it2.setPackage("com.zch.serviceapp");
                bindService(it2,conn,BIND_AUTO_CREATE);
                break;

            case R.id.unbind:
                //远程解绑服务
                unbindService(conn);
                break;

            case R.id.stop:
                //远程停止服务
                Intent it1 = new Intent();
                it1.setAction("com.zch.myservice");
                it1.setPackage("com.zch.serviceapp");
                stopService(it1);
                break;
        }
    }
}
