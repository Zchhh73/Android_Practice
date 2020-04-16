package com.zch.restapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.CookieJar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zch.restapp.R;
import com.zch.restapp.UserInfoHolder;
import com.zch.restapp.bean.User;
import com.zch.restapp.biz.UserBiz;
import com.zch.restapp.net.CommonCallback;
import com.zch.restapp.utils.T;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

public class LoginActivity extends BaseActivity {



    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private TextView mTvRegister;

    private UserBiz mUserBiz = new UserBiz();

    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PASSWORD = "key_password";




    @Override
    protected void onResume() {
        super.onResume();
        CookieJarImpl cookieJar=(CookieJarImpl) OkHttpUtils
                .getInstance().getOkHttpClient().cookieJar();
        cookieJar.getCookieStore().removeAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        initEvent();

        initIntent(getIntent());


    }

    private void initView() {
        mEtUsername=(EditText) findViewById(R.id.id_et_username);
        mEtPassword=(EditText) findViewById(R.id.id_et_password);
        mBtnLogin=(Button) findViewById(R.id.id_btn_login);
        mTvRegister=(TextView) findViewById(R.id.id_tv_register);
    }

    private void initEvent() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //取参数
                String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();
                //客户端校验
                if(TextUtils.isEmpty(username)||TextUtils.isEmpty(username)){
                    T.showToast("账号或密码不能为空");
                    return;
                }

                startLoadingProgress();

                //发请求
                mUserBiz.login(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(User response) {
                        stopLoadingProgress();
                        T.showToast("登录成功");
                        //保存用户登录信息
                        UserInfoHolder.getInstance().setUser(response);
                        toOrderActivity();
                    }
                });

            }
        });

        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRegisterActivity();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initIntent(intent);
    }

    private void initIntent(Intent intent) {
        if(intent == null){
            return;
        }

        String userName = intent.getStringExtra(KEY_USERNAME);
        String password = intent.getStringExtra(KEY_PASSWORD);

        if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(password)){
            return;
        }
        mEtUsername.setText(userName);
        mEtPassword.setText(password);

    }

    private void toOrderActivity() {
        Intent intent = new Intent(this,OrderActivity.class);
        startActivity(intent);
        finish();
    }


    private void toRegisterActivity() {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    public static void launch(Context context, String username, String password) {
        Intent intent = new Intent(context,LoginActivity.class);
        //清除栈顶的东西，singleTask
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(KEY_USERNAME,username);
        intent.putExtra(KEY_PASSWORD,password);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserBiz.onDestory();
    }
}
