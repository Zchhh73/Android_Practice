package com.zch.timeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TimeActivity extends AppCompatActivity {
    private TextView title, timer, txt;
    private ImageView btn;
    private boolean flag = false; //用于区别当前对按钮的点击是开启or停止。
    private Handler handler=new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //获取分秒
            int min=msg.arg1 / 60;
            int sec=msg.arg1 % 60;
            //00:00
            String time=(min < 10 ? "0" + min : "" + min) + ":"
                    + (sec < 10 ? "0" + sec : "" + sec);
            timer.setText(time);

            if(flag==false){
                title.setText("计时器");
                btn.setImageResource(R.mipmap.start);
                txt.setText("用时："+time);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        title=findViewById(R.id.time_title);
        timer=findViewById(R.id.time_text);
        txt=findViewById(R.id.start_text);

        btn=findViewById(R.id.start_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == false){
                    //静止状态，开始计时
                    flag = true;
                    title.setText("工作中");
                    btn.setImageResource(R.mipmap.stop);
                    txt.setText("");
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            int i=1;
                            //时间增长
                            while (flag) {
                                try {
                                    sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message msg=new Message();
                                msg.arg1=i;
                                handler.sendMessage(msg);
                                //时间增长
                                i++;
                            }
                        }
                    }.start();
                }else{
                    flag = false;
                }

            }
        });

    }
}
