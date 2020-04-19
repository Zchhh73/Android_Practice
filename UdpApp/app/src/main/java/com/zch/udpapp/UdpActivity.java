package com.zch.udpapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zch.udpapp.biz.UdpClientBiz;

public class UdpActivity extends AppCompatActivity {
    
    private EditText mEtMsg;
    private Button mBtnSend;
    private TextView mTvContent;

    private UdpClientBiz udpClientBiz = new UdpClientBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mEtMsg.getText().toString();
                if(TextUtils.isEmpty(msg)){
                    return;
                }

                appendMsg("client:"+msg);
                mEtMsg.setText("");

                udpClientBiz.sendMsg(msg, new UdpClientBiz.onMsgReturnedListener() {
                    @Override
                    public void onMsgReturned(String msg) {
                        appendMsg("server:"+msg);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });

            }
        });
    }

    private void appendMsg(String msg){
        mTvContent.append(msg + "\n");
    }

    private void initViews() {
        mEtMsg = findViewById(R.id.id_et_msg);
        mBtnSend = findViewById(R.id.id_btn_send);
        mTvContent = findViewById(R.id.id_tv_content);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        udpClientBiz.onDestroy();
    }
}
