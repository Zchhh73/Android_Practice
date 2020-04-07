package com.zch.datastoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ShareActivity extends AppCompatActivity {

    private EditText accEdt, pwdEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        accEdt=findViewById(R.id.et_name);
        pwdEdt=findViewById(R.id.et_pwd);

        //sharePreference读取
        //获取SharePreference对象(文件名，模式)
        SharedPreferences share = getSharedPreferences("myshare",MODE_PRIVATE);
        String accStr = share.getString("account","");
        String pwdStr = share.getString("pwd","");

        accEdt.setText(accStr);
        pwdEdt.setText(pwdStr);



        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.获取输入框内容
                String account=accEdt.getText().toString();
                String pwd=pwdEdt.getText().toString();

                //2.验证(admin 123)

                if (account.equals("admin") && pwd.equals("123")) {
                    //存储到SharePreference
                    //获取SharePreference对象(文件名，模式)
                    SharedPreferences share = getSharedPreferences("myshare",MODE_PRIVATE);
                    //获取Editor对象
                    SharedPreferences.Editor edt = share.edit();
                    //存储信息对象
                    edt.putString("account",account);
                    edt.putString("pwd",pwd);
                    //指定提交操作对象
                    edt.commit();


                    Toast.makeText(ShareActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                } else {
                    //验证失败，提示用户
                    Toast.makeText(ShareActivity.this, "账户或密码错误", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
