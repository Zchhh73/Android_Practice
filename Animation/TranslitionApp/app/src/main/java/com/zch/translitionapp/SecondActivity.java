package com.zch.translitionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.widget.ImageView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        int resId = getIntent().getExtras().getInt("resId");
        ImageView iv = findViewById(R.id.iv);
        iv.setImageResource(resId);

        Transition transition = new Explode();
        transition.excludeTarget(android.R.id.statusBarBackground,true);
        getWindow().setEnterTransition(transition);
        getWindow().setExitTransition(transition);
    }
}
