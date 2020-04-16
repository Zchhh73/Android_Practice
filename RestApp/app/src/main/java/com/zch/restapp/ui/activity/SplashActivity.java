package com.zch.restapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.zch.restapp.R;

public class SplashActivity extends AppCompatActivity {

    private Button mBtnSkip;

    //等待3秒跳过
    private Handler mHandler = new Handler();

    private Runnable mRunnableToLogin = new Runnable() {
        @Override
        public void run() {
            toLoginActivity();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
        initEvent();

        mHandler.postDelayed(mRunnableToLogin,3000);


    }

    private void initEvent() {
        mBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacks(mRunnableToLogin);
                toLoginActivity();
            }
        });
    }

    private void initView() {
        mBtnSkip = (Button) findViewById(R.id.id_btn_skip);

    }

    /**
     * 跳转登录界面
     */
    private void toLoginActivity(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止handler内存泄露
        mHandler.removeCallbacks(mRunnableToLogin);
    }
}
