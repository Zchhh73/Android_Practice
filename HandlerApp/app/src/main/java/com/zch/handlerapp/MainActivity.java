package com.zch.handlerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button handler_btn;
    private TextView textView;
    private String str="";
    //1.实例化
    //2.在子线程中发送消息

    //UI线程调用handler
    private Handler handler=new Handler() {
        //3.由Handler对象接收消息，并处理
        //只要handler发消息，就触发该方法，并传入一个Message对象。

        /**
         * 回调方法。
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //btn1
                textView.setText(str);
            } else if (msg.what == 2) {
                //btn2
                String str2="msg" + msg.what + ",arg1:" + msg.arg1
                        + ",arg2:" + msg.arg2 + ",obj:" + ((Random) msg.obj).nextInt();
                Toast.makeText(MainActivity.this, str2, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler handler2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler_btn=findViewById(R.id.btn1);
        textView=findViewById(R.id.handle_txt);


        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();//准备开始消息循环。系统自动为主线程开启消息循环。
                handler2 = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Log.e("TAG", "handler2.what:"+msg.what);
                    }
                };
                Looper.loop();//循环，等于产生while(true)消息循环
            }
        }.start();
    }

    public void myClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        str=get();
                        Log.e("TAG", str);
                        //发消息
                        //handler.sendMessage();
                        //发空消息,触发回调
                        //参数what:
                        handler.sendEmptyMessage(1);
//                        textView.setText(msg);
                    }
                }.start();
                break;
            case R.id.btn2:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        str=get() + "--------";
                        Message msg=new Message();
                        //what:用于区分handler发现消息的不同线程来源
                        //arg1,arg2:如果子线程需要向主线程传递整型数据，使用该参数
                        //obj：Object
                        msg.what=2;
                        msg.arg1=666;
                        msg.arg2=2333;
                        msg.obj=new Random();
//                        handler.sendEmptyMessage(2);
                        handler.sendMessage(msg);

                    }
                }.start();
                break;
            case R.id.btn3:
                handler2.sendEmptyMessage(1000);
                break;
        }
    }

    private String get() {
        try {
            URL url=new URL("http://www.imooc.com/api/teacher?type=3&cid=1");
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in=conn.getInputStream();
                byte[] b=new byte[1024];
                int len=0;
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                while ((len=in.read(b)) > -1) {
                    baos.write(b, 0, len);
                }
                String msg=new String(baos.toByteArray());
                return msg;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }


}
