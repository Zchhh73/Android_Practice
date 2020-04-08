package com.zch.broaddemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG="MyBroadcastReceiver";
    TextView mTextView;

    public MyBroadcastReceiver() {
    }

    public MyBroadcastReceiver(TextView mTextView) {
        this.mTextView=mTextView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //接收广播
        if(intent != null){
            //接收什么广播
            String action = intent.getAction();
            Log.d(TAG, "onReceive: "+action);
            //判断自定义广播
            if(TextUtils.equals(action,MainActivity.MY_ACTION)){
                //获取广播携带数据
                String content = intent.getStringExtra(MainActivity.BROADCAST_CONTENT);
                if(mTextView != null){
                    mTextView.setText("接收到的action:"+action+"\n接收到的内容是："+content);
                }
            }
        }
    }

}
