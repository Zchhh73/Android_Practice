package com.zch.okhttpapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zch.okhttpapp.net.okhttp.INetCallBack;
import com.zch.okhttpapp.net.okhttp.OkHttpUtils;
import com.zch.okhttpapp.net.okhttp.OkHttpUtils2;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Button mBtnGet, mBtnPost,mBtnPostMulti,mBtnPostJson;
    private TextView mTvContent;

    private Handler mUiHandler=new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvents();
    }

    private void initEvents() {
        mBtnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,String> headers = new HashMap<>();
                headers.put("author_header","zch");

                OkHttpUtils2.getInstance()
                        .doGet("http://www.imooc.com/api/okhttp/getmethod?username=zch",
                                headers,
                                new INetCallBack() {
                            @Override
                            public void onSuccess(String response) {
                                mTvContent.setText(response);
                            }

                            @Override
                            public void onFailed(Throwable ex) {
                                Toast.makeText(MainActivity.this, "网络发生错误", Toast.LENGTH_SHORT).show();
                            }
                        });

                /*
                //发起网络请求
                new Thread() {
                    @Override
                    public void run() {
                        String content=OkHttpUtils.getInstance()
                                .doGet("http://www.imooc.com/api/okhttp/getmethod?username=zch");
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                }.start();
               */
            }
        });

        mBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,String> headers = new HashMap<>();
                headers.put("author_header","zch");

                HashMap<String,String> params = new HashMap<>();
                params.put("author","zch");
                params.put("abc","xyz");


                OkHttpUtils2.getInstance().doPost("http://www.imooc.com/api/okhttp/postmethod",
                        headers,
                        null,
                        new INetCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        mTvContent.setText(response);
                    }

                    @Override
                    public void onFailed(Throwable ex) {
                        Toast.makeText(MainActivity.this, "网络发生错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mBtnPostMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("author_header","zch");

                HashMap<String,String> params = new HashMap<>();
                params.put("author","zch");
                params.put("abc","xyzmultipart");

                OkHttpUtils2.getInstance().doPostMultiPart("http://www.imooc.com/api/okhttp/postmethod",
                        headers,
                        params,
                        new INetCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        mTvContent.setText(response);
                    }

                    @Override
                    public void onFailed(Throwable ex) {
                        Toast.makeText(MainActivity.this, "网络发生错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mBtnPostJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,String> headers = new HashMap<>();
                headers.put("author_header","zch");

                OkHttpUtils2.getInstance().doPostJson("http://www.imooc.com/api/okhttp/postjson",
                        headers,
                        "{\"name\":\"zch\",\"age\":\"12\"}",
                        new INetCallBack() {
                            @Override
                            public void onSuccess(String response) {
                                mTvContent.setText(response);
                            }

                            @Override
                            public void onFailed(Throwable ex) {
                                Toast.makeText(MainActivity.this, "网络发生错误", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void initView() {
        mBtnGet=findViewById(R.id.btn_get);
        mBtnPost=findViewById(R.id.btn_post);
        mBtnPostMulti = findViewById(R.id.btn_post_multi);
        mBtnPostJson = findViewById(R.id.btn_post_json);
        mTvContent=findViewById(R.id.tv_content);

    }
}
