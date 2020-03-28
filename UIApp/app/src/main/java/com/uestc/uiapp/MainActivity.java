package com.uestc.uiapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userPwd;
    private Button register_btn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = (EditText)findViewById(R.id.name);
        userPwd = (EditText)findViewById(R.id.pwd);
        register_btn = (Button)findViewById(R.id.register);
        progressBar = (ProgressBar)findViewById(R.id.pro_bar);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.判断姓名密码是否为空
                String name = userName.getText().toString();
                String pwd = userPwd.getText().toString();
                if(userName.equals("")||pwd.equals("")){
                    //2.如果为空，则提示,无焦点提示
                    Toast.makeText(MainActivity.this, "姓名或密码不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    //3.如果不空，则出现进度条
                    progressBar.setVisibility(View.VISIBLE);
                    new Thread(){
                        @Override
                        public void run() {
                            for(int i = 0;i<=100;i++){
                                progressBar.setProgress(i);
                                try {
                                    Thread.sleep(30);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.start();
                }


            }
        });


    }
}
