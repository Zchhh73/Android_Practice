package com.zch.restapp.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zch.restapp.R;
import com.zch.restapp.bean.User;
import com.zch.restapp.biz.UserBiz;
import com.zch.restapp.net.CommonCallback;
import com.zch.restapp.utils.T;

import org.w3c.dom.Text;

public class RegisterActivity extends BaseActivity {

    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mEtRePassword;
    private Button mBtnRegister;

    private UserBiz mUserBiz = new UserBiz();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        //toolbar
        setUpToolbar();
        
        initView();
        initEvent();

        setTitle("注册");
    }



    private void initEvent() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();
                String repassword = mEtRePassword.getText().toString();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    T.showToast("账号或密码不能为空");
                    return;
                }

                if(!password.equals(repassword)){
                    T.showToast("两次输入密码不一致");
                    return;
                }

                startLoadingProgress();

                mUserBiz.register(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(User response) {
                        stopLoadingProgress();
                        T.showToast("注册成功，用户名为:"+response.getUsername());
                        //携带用户名密码跳转到登录页

                        LoginActivity.launch(RegisterActivity.this,response.getUsername(),response.getPassword());
                        finish();
                    }
                });
            }
        });
    }

    private void initView() {
        mEtUsername = findViewById(R.id.id_et_reg_username);
        mEtPassword = findViewById(R.id.id_et_reg_password);
        mEtRePassword = findViewById(R.id.id_et_reg_repassword);
        mBtnRegister = findViewById(R.id.id_btn_register);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserBiz.onDestory();
    }
}
