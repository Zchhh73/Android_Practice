package com.zch.networkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button get_btn;
    private EditText name_et;
    private EditText pwd_et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        get_btn=findViewById(R.id.get);
        name_et=findViewById(R.id.name_txt);
        pwd_et=findViewById(R.id.pwd_txt);


    }

    public void myClick(View view) {
        switch (view.getId()) {
            case R.id.get:
                //在子线程中进行网络请求
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        get();
                    }
                }.start();
                break;
            case R.id.post:
                final String name = name_et.getText().toString();
                final String pwd = pwd_et.getText().toString();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        post(name,pwd);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(MainActivity.this,JSONActivity.class));
                            }
                        });
                    }
                }.start();
                break;

        }
    }

    private void get() {
        //HttpURLConnection
        //1.实例化URL对象
        try {
            URL url=new URL("http://www.imooc.com/api/teacher?type=3&cid=1");

            //2.获取HttpURLConnetion实例
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            //3.设置请求相关属性
            //请求方式
            conn.setRequestMethod("GET");
            //请求超时时长
            conn.setConnectTimeout(6000);

            //4.获取响应码（发送请求）  200 404未请求到 500服务器异常
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //5.判断响应码并获取响应数据(响应正文)
                //获取响应的流
                InputStream in=conn.getInputStream();
                //在循环中读取输入流
                byte[] b=new byte[1024];//1G
                int len=0;
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                while ((len=in.read(b)) > -1) {
                    //将字节数组存入缓存流,len实际读到的长度。
                    baos.write(b, 0, len);
                }
                String msg=new String(baos.toByteArray());
                Log.e("TAG", msg + "===============");

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void post(String username, String userpwd) {
        //HttpURLConnection
        //1.实例化URL对象
        try {
            URL url=new URL("http://www.imooc.com/api/okhttp/postmethod");

            //2.获取HttpURLConnetion实例
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            //3.设置请求相关属性
            //请求方式
            conn.setRequestMethod("POST");
            //请求超时时长
            conn.setConnectTimeout(6000);
            //设置允许输出
            conn.setDoOutput(true);
            //设置提交数据类型
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            //设置输出流(请求正文)
            OutputStream out=conn.getOutputStream();
            //写
            out.write(("username="+username+"&pwd="+userpwd).getBytes());


            //4.获取响应码（发送请求）  200 404未请求到 500服务器异常
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //5.判断响应码并获取响应数据(响应正文)
                //获取响应的流
                InputStream in=conn.getInputStream();
                //在循环中读取输入流
                byte[] b=new byte[1024];//1G
                int len=0;
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                while ((len=in.read(b)) > -1) {
                    //将字节数组存入缓存流,len实际读到的长度。
                    baos.write(b, 0, len);
                }
                String msg=new String(baos.toByteArray());
                Log.e("TAG", msg + "===============");

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
