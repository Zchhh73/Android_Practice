package com.zch.okhttpapp.net.okhttp.interceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        //实际开发可以添加token..
        Request orginRequest=chain.request();
        Request newRequest=orginRequest.newBuilder()
                .addHeader("author", "zch_interceptor")
                .build();

        return chain.proceed(newRequest);

    }
}
