package com.zch.translitionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Pair;
import android.view.View;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    public void onClick(View v){
        int resId = -1;
        switch (v.getId()){
            case R.id.iv1:
                resId = R.drawable.pic1;
                break;
            case R.id.iv2:
                resId = R.drawable.pic2;
                break;
            case R.id.iv3:
                resId = R.drawable.pic3;
                break;
            case R.id.iv4:
                resId = R.drawable.pic4;
                break;
        }
        Intent intent = new Intent(this,SecondActivity.class);
        intent.putExtra("resId",resId);
        //设置转场
        Transition transition = new Explode();
        //排除id保持不动，状态栏
        transition.excludeTarget(android.R.id.statusBarBackground,true);
        //进场动画
        getWindow().setEnterTransition(transition);
        //离场动画
        getWindow().setExitTransition(transition);
        //回场动画
        getWindow().setReenterTransition(transition);


        //共享元素
        Pair<View,String> shareElement =Pair.create(v,"img");

        //传递给option对象
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,shareElement);
        startActivity(intent,options.toBundle());
    }
}
