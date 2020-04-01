package com.zch.fragmenttest;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class TabFragmentActivity extends AppCompatActivity implements Fragment3.MyListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_fragment);

        //默认加载首页
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.add添加fragment
        transaction.add(R.id.tab_container,new IndexFragment());
        transaction.commit();
    }

    public void myClick(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.replace切换fragment
        switch (view.getId()){
            case R.id.rb_index:
                transaction.replace(R.id.tab_container,new IndexFragment());
                break;
            case R.id.rb_channel:
                //setArguments传值
                //1.实例化Fragment
                Fragment f1 = new Fragment1();
                //2.实例化Bundle对象
                Bundle bundle = new Bundle();
                //3.存入数据到Bundle
                bundle.putString("msg1","这是由Activity传往Fragment的数据");
                //4.调用Fragment的setArgument方法，传入Bundle
                f1.setArguments(bundle);
                transaction.replace(R.id.tab_container,f1);
                break;
            case R.id.rb_list:
                transaction.replace(R.id.tab_container,new Fragment2());
                break;
            case R.id.rb_me:

                transaction.replace(R.id.tab_container,new Fragment3());
                break;
        }
        transaction.commit();
    }

    public String sendMsg(){
        return "这是通过一个方法传递的消息";
    }

    @Override
    public void sendMsg(String msg) {
        Log.e("TAG", "Fragment3传回的数据"+msg );
    }
}
