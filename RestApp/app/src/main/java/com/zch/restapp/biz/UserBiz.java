package com.zch.restapp.biz;

import com.zch.restapp.bean.User;
import com.zch.restapp.config.Config;
import com.zch.restapp.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

public class UserBiz {

    public void login(String username, String password,
                      CommonCallback<User> commonCallback){

        OkHttpUtils
                .post()
                .url(Config.baseUrl + "user_login")
                .tag(this)
                .addParams("username",username)
                .addParams("password",password)
                .build()
                .execute(commonCallback);


    }

    public void register(String username, String password,
                         CommonCallback<User> commonCallback){
        OkHttpUtils
                .post()
                .url(Config.baseUrl + "user_register")
                //cancel某一个请求
                .tag(this)
                .addParams("username",username)
                .addParams("password",password)
                .build()
                .execute(commonCallback);
    }

    public void onDestory(){
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
