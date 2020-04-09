package com.zch.retrofitapp.net.retrofit;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitImpl {
    private OkHttpClient mOkHttpclient;
    private Retrofit mRetrofit;

    public static Retrofit getRetrofit(){
        return sInstance.mRetrofit;
    }

    //单例
    private static RetrofitImpl sInstance = new RetrofitImpl();

    public static RetrofitImpl getInstance(){
        return sInstance;
    }

    private RetrofitImpl() {

        HttpLoggingInterceptor logging=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String s) {
                Log.d("zch", s);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpclient=new OkHttpClient.Builder()
//                .connectTimeout()
                //打印构造的请求结果
//                .addInterceptor()
                .addInterceptor(logging)
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(mOkHttpclient)
                .addConverterFactory(GsonConverterFactory.create())
                //组成实际path
                .baseUrl("http://www.imooc.com/api/okhttp/")
                .build();
    }
}
