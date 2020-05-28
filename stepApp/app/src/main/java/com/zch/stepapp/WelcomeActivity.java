package com.zch.stepapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends BaseActivity {

    private Handler mHandler;
    private Runnable jumpRunnable;


    @Override
    protected void onInitVariable() {
        mHandler = new Handler();
        jumpRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_welcome);

    }

    @Override
    protected void onRequestData() {

        mHandler.postDelayed(jumpRunnable,3000);

    }
}
