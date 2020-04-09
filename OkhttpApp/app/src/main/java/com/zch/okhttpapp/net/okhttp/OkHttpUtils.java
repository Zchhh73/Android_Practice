package com.zch.okhttpapp.net.okhttp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUtils {

    private OkHttpUtils(){}

    private static OkHttpUtils sInstance = new OkHttpUtils();

    public static OkHttpUtils getInstance(){
        return sInstance;
    }



    public String doGet(String url) {
        try {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Call call = client.newCall(request);
            Response response=call.execute();
            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
