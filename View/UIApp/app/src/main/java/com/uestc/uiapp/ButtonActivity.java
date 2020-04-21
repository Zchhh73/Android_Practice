package com.uestc.uiapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ButtonActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        Button btn1 = findViewById(R.id.btn1);
        MyClickListener mcl = new MyClickListener();
        btn1.setOnClickListener(mcl);

        //匿名内部类适用有唯一操作的按钮
        Button btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "匿名内部类监听器对象");
            }
        });

        Button btn3 = findViewById(R.id.btn3);
        btn3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Log.e("TAG", "用本类实现监听器对象");
    }

    //自定义点击事件类
    class MyClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Log.e("TAG", "内部类监听器对象");
        }
    }

    public void myClick(View v){
        Log.e("TAG", "用xml类实现监听器对象");
    }
}
