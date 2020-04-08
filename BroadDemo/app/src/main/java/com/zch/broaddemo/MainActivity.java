package com.zch.broaddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String MY_ACTION="com.zch.boarddemo.aaa";
    public static final String BROADCAST_CONTENT="broadcast_content";
    MyBroadcastReceiver myBroadcastReceiver;

    private EditText input_et;
    private Button btn_send;
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getPackageName());

        input_et = findViewById(R.id.input_et);
        btn_send = findViewById(R.id.send_btn);
        tv_result = findViewById(R.id.tv_result);



        //新建广播接收器
        myBroadcastReceiver=new MyBroadcastReceiver(tv_result);
        //注册广播接收器

        //接收的广播
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(MY_ACTION);

        //注册广播接收器
        registerReceiver(myBroadcastReceiver, intentFilter);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新建广播
                Intent intent = new Intent(MY_ACTION);
                //放入数据
                intent.putExtra(BROADCAST_CONTENT,input_et.getText().toString());
                sendBroadcast(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册广播接收器
        if (myBroadcastReceiver != null) {
            unregisterReceiver(myBroadcastReceiver);
        }
    }
}
