package com.zch.client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zch.simpleaidl.IRemoteCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBindBtn;
    private Button mUnbindBtn;
    private Button mSendRequestBtn;
    private TextView mResultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mBindBtn=findViewById(R.id.bindBtn);
        mUnbindBtn=findViewById(R.id.unbindBtn);
        mSendRequestBtn=findViewById(R.id.sendRequestBtn);
        mResultTv=findViewById(R.id.resultTv);

        mBindBtn.setOnClickListener(this);
        mUnbindBtn.setOnClickListener(this);
        mSendRequestBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bindBtn:
                AIDLUtil.getInstance().bindService(MainActivity.this, new IRemoteCallback.Stub() {
                    @Override
                        public void onSuccess(final String func, final String params) throws RemoteException {
                            Log.i("MainActivity", func + params);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mResultTv.append("func:"+func+"\n");
                                mResultTv.append("params:"+params+"\n");
                            }
                        });

                    }

                    @Override
                    public void onError(String func, int errorCode) throws RemoteException {

                    }
                });
                mResultTv.append("绑定服务");
                mResultTv.append("\n");
                break;
            case R.id.unbindBtn:
                AIDLUtil.getInstance().unbindService(MainActivity.this);
                mResultTv.append("解除绑定\n");
                break;
            case R.id.sendRequestBtn:
//                String request = AIDLUtil.getInstance().request("char","");
//                if(!TextUtils.isEmpty(request)){
//                    mResultTv.append("接收到返回数据：");
//                    mResultTv.append(request);
//                    mResultTv.append("\n");
//                }
                AIDLUtil.getInstance().send(MainActivity.this);
                break;
        }
    }
}
