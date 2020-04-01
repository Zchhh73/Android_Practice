package com.zch.fragmenttest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("TAG", "Activity-onCreate: ");

        //fragment管理器，FragmentTransaction事务

        //1.获取管理器
        FragmentManager manager=getSupportFragmentManager();

        //2.获取事务（开启事务）
        FragmentTransaction transaction=manager.beginTransaction();

        //3.动态添加Fragment 参数：容器id，Fragment对象
        final Fragment fragment2=new Fragment2();
        transaction.add(R.id.container,fragment2);

        //提交事务。
        transaction.commit();

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.获取Fragment管理器
                FragmentManager fragmentManager=getSupportFragmentManager();
                //2.获取Fragment对象
                Fragment f1=fragmentManager.findFragmentById(R.id.fragment1);
                //3.获取Fragment对象视图
                View v1=f1.getView();
                //4.在视图里找到指定控件
                TextView tv=v1.findViewById(R.id.text1);
                //5.修改控件内容
                tv.setText("这是动态生成的内容");


                View v2 = fragment2.getView();
                TextView txt2 = v2.findViewById(R.id.text2);
                txt2.setText("这是fragment2动态生成的内容");



            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TAG", "Activity-onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG", "Activity-onResume: ");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TAG", "Activity-onPause: ");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TAG", "Activity-onStop: ");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TAG", "Activity-onDestroy: ");

    }
}
