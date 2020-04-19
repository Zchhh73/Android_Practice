package com.zch.udpapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zch.udpapp.biz.TcpClientBiz;
import com.zch.udpapp.https.HttpUtils;

import androidx.appcompat.app.AppCompatActivity;

public class HttpActivity extends AppCompatActivity {
    private EditText mEtMsg;
    private Button mBtnSend;
    private TextView mTvContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp);

        initViews();

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mEtMsg.getText().toString();
                if(TextUtils.isEmpty(url)){
                    return;
                }

                HttpUtils.doGet(getApplicationContext(),url, new HttpUtils.HttpListener() {
                    @Override
                    public void onSuccess(String content) {
                        mTvContent.setText(content);
                    }

                    @Override
                    public void onFail(Exception e) {
                        e.printStackTrace();

                    }
                });
            }
        });



    }

    private void initViews() {
        mEtMsg = findViewById(R.id.id_et_msg);
        mBtnSend = findViewById(R.id.id_btn_send);
        mTvContent = findViewById(R.id.id_tv_content);
    }


}
