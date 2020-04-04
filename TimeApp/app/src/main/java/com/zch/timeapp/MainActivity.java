package com.zch.timeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void myClick(View view){
        switch (view.getId()){
            case R.id.btn_click:
                startActivity(new Intent(MainActivity.this,TimeActivity.class));
                break;
        }
    }
}
