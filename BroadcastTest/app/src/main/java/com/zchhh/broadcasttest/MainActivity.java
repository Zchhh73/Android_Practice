package com.zchhh.broadcasttest;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button send;

    private Button local_broad;
    private MyReceiver myReceiver;
    private LocalBroadcastManager localBroadcastManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1.获取实例
        localBroadcastManager = LocalBroadcastManager.getInstance(this);//实例
        //动态注册本地广播监听器：
        //1.重写oncreate，加入registerReceiver,使用registerReceiver动态注册。
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.zchhh.broadcast.MY_BROADCAST");
        myReceiver = new MyReceiver();
        localBroadcastManager.registerReceiver(myReceiver,intentFilter);

        send = (Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.zchhh.broadcast.MY_BROADCAST");
                //LocalBroadcastManager提供的sendBroadcast发送本地广播
                localBroadcastManager.sendBroadcast(intent);

            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //2.重写ondestroy，使用unregisterReceiver取消接收器。
        localBroadcastManager.unregisterReceiver(myReceiver);
    }
}
