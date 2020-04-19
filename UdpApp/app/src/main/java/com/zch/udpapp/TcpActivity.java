package com.zch.udpapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zch.udpapp.biz.TcpClientBiz;

public class TcpActivity extends AppCompatActivity {
    private EditText mEtMsg;
    private Button mBtnSend;
    private TextView mTvContent;

    private TcpClientBiz mTcpClientBiz = new TcpClientBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp);

        initViews();

        mTcpClientBiz.setonTmsgCommingListner(new TcpClientBiz.onTmsgCommingListner() {
            @Override
            public void onMsgComing(String msg) {
                appendMsg("client:"+msg);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mEtMsg.getText().toString();
                if(TextUtils.isEmpty(msg)){
                    return;
                }

                mEtMsg.setText("");
                mTcpClientBiz.sendMsg(msg);
            }
        });

    }

    private void initViews() {
        mEtMsg = findViewById(R.id.id_et_msg);
        mBtnSend = findViewById(R.id.id_btn_send);
        mTvContent = findViewById(R.id.id_tv_content);
    }

    private void appendMsg(String msg){
        mTvContent.append(msg + "\n");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTcpClientBiz.onDestroy();
    }
}
