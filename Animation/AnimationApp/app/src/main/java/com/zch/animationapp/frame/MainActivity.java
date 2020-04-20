package com.zch.animationapp.frame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.zch.animationapp.R;

public class MainActivity extends AppCompatActivity {

    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        View view = findViewById(R.id.view);
        animationDrawable=(AnimationDrawable) view.getBackground();
        //完成一次就停止
//        animationDrawable.setOneShot(true);

    }

    public void onClick(View v){

        switch (v.getId()){
            case R.id.btnStart:
                animationDrawable.start();
                break;
            case R.id.btnStop:
                animationDrawable.stop();
                break;
    }
        
    }
}
