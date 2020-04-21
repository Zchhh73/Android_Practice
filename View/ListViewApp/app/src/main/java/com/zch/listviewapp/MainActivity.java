package com.zch.listviewapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
            case R.id.btn1:
                startActivity(new Intent(this,ArrayActivity.class));
                break;

            case R.id.btn2:
                startActivity(new Intent(this,SimpleActivity.class));
                break;
            case R.id.btn3:
                startActivity(new Intent(this,BaseActivity.class));

                break;
        }
    }
}
