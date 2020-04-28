package com.uestc.bookreader;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.lang.ref.WeakReference;

import cz.msebera.android.httpclient.Header;

public class SplashActivity extends Activity {

    public static final int CODE = 1001;
    //总时间
    public static final int TOTAL_TIME = 3000;
    public static final int INTERVAL_TIME = 1000;

    private TextView mtextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        mtextView=(TextView)findViewById(R.id.time_text_view);

        final MyHandler handler = new MyHandler(this);
        Message message = Message.obtain();
        message.what = CODE;
        message.arg1 =TOTAL_TIME;
        handler.sendMessage(message);

        mtextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookListActivity.start(SplashActivity.this);
                SplashActivity.this.finish();
                //释放handler
                handler.removeMessages(CODE);
            }
        });

    }

    /**
     * 防止Handler内存泄露，弱引用：当系统回收时，如果未被调用会被回收
     */
    private static class MyHandler extends Handler{
        public final WeakReference<SplashActivity> mWeakReference;

        public MyHandler(SplashActivity activity){
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SplashActivity activity = mWeakReference.get();
            if(msg.what==CODE){

                if(activity != null){
                    //接收秒数
                    int time = msg.arg1;
                    //设置TextView,更新UI
                    activity.mtextView.setText(time/INTERVAL_TIME+"秒，点击跳过");

                    //发送倒计时

                    Message message = Message.obtain();
                    message.what = CODE;
                    message.arg1 = time - INTERVAL_TIME;
                    //间隔时间发送
                    if(time>0){
                        sendMessageDelayed(message,INTERVAL_TIME);
                    }else{
                        //跳到下一页
                        BookListActivity.start(activity);
                        activity.finish();
                    }

                }
            }
        }
    }
}
