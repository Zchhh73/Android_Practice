package com.zch.translitionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";

    private View view;
    private CheckBox mCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.view);
        mCheckBox = findViewById(R.id.checkBox);

    }

    public void onClick(View v){
        boolean play = mCheckBox.isChecked();
        switch (v.getId()){
            case R.id.buttonChangeVisibility:
                handleChangeVisibility(play);
                break;
        }

    }

    private void handleChangeVisibility(boolean play) {
        Log.d(TAG, "handleChangeVisibility: "+view.isShown());
        if(play){
            //如果播放动画
            if(view.isShown()){
                revealExit();
            }else{
                revealEnter();
            }

        }else{
            //false,若可见，变为不可见
            if(view.isShown()){
                view.setVisibility(View.INVISIBLE);
            }else{
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    private void revealEnter() {

        int w = view.getWidth();
        int h = view.getHeight();

        //坐标
        int cx = w;
        int cy = h;
        int r = (int)Math.hypot(w,h);
        Animator animator =ViewAnimationUtils.createCircularReveal(view,cx,cy,0,r);

        view.setVisibility(View.VISIBLE);
        animator.start();

    }


    private void revealExit() {

        int w = view.getWidth();
        int h = view.getHeight();

        int cx = w;
        int cy = h;

        int r = (int)Math.hypot(w,h);

        Animator animator = ViewAnimationUtils.createCircularReveal(view,cx,cy,r,0);
        animator.setDuration(5000);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();

    }
}
