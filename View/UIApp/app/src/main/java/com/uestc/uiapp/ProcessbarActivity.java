package com.uestc.uiapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class ProcessbarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processbar);
        final ProgressBar progressBar = findViewById(R.id.progress);

        //在4.0后不能直接在线程中操作控件的，进度条特例
        new Thread(){
            @Override
            public void run() {
                for(int i =1;i<=100;i++){
                    progressBar.setProgress(i);
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
