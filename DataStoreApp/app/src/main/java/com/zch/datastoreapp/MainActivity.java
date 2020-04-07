package com.zch.datastoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btn_share;
    private Button btn_filein;
    private Button btn_fileout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_share = findViewById(R.id.btn_share);
        btn_filein = findViewById(R.id.btn_filein);
        btn_fileout = findViewById(R.id.btn_fileout);

    }

    public void myClick(View v){
        switch (v.getId()){
            case R.id.btn_share:
                startActivity(new Intent(MainActivity.this,ShareActivity.class));
                finish();
                break;
            case R.id.btn_fileout:
                startActivity(new Intent(MainActivity.this,ExternalActivity.class));
                finish();
            case R.id.btn_filein:
                startActivity(new Intent(MainActivity.this,InternalActivity.class));
                finish();
        }
    }
}
