package com.zch.stepapp;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.zch.frame.FrameApplication;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public abstract class BaseActivity extends FragmentActivity {

    //是否显示程序标题
    protected boolean isHideAppTitle = true;
    //是否显示系统标题
    protected boolean isHideSysTitle = false;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            this.onInitVariable();
            if(this.isHideAppTitle){
                this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            }
            super.onCreate(savedInstanceState);
            if(this.isHideSysTitle){
                this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }

        //构造View,绑定事件
        this.onInitView(savedInstanceState);
        this.onRequestData();
        FrameApplication.addToActivityList(this);
    }

    //初始化变量，创建对象
    protected abstract void onInitVariable();

    //布局初始化,布局载入
    protected abstract void onInitView(Bundle savedInstanceState);

    //请求数据
    protected abstract void onRequestData();

    @Override
    protected void onDestroy() {
        FrameApplication.removeFromActivityList(this);
        super.onDestroy();
    }
}
