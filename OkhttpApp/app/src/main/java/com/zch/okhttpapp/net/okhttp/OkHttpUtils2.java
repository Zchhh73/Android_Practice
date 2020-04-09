package com.zch.okhttpapp.net.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.zch.okhttpapp.R;
import com.zch.okhttpapp.net.okhttp.interceptor.AuthInterceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpUtils2 {

    private Handler mUiHandler=new Handler(Looper.getMainLooper());
    private OkHttpClient mOkHttpclient;


    private static OkHttpUtils2 sInstance=new OkHttpUtils2();

    private OkHttpUtils2() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NotNull String s) {
                Log.d("zch", s);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpclient = new OkHttpClient.Builder()
//                .connectTimeout()
                //打印构造的请求结果
                .addInterceptor(new AuthInterceptor())
                .addInterceptor(logging)
                .build();
    }
    public static OkHttpUtils2 getInstance() {
        return sInstance;
    }


    public void doGet(String url, HashMap<String,String> headers,INetCallBack callBack) {

        //添加header信息
        Request.Builder builder = new Request.Builder();
        if(headers != null){
            for(String key:headers.keySet()){
                builder.addHeader(key,headers.get(key));
            }
        }
        Request request=builder
                .url(url)
                .build();

        Call call=mOkHttpclient.newCall(request);

        //异步方式
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed(e);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String respStr=null;
                try {
                    respStr=response.body().string();
                } catch (IOException e) {
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailed(e);
                        }
                    });
                    return;
                }
                String finalRespStr = respStr;
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(finalRespStr);
                    }
                });

            }
        });


    }

    public void doPost(String url, HashMap<String,String> headers,HashMap<String,String> params, INetCallBack callBack){

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if(params != null){
            for(String param : params.keySet()){
                formBodyBuilder.add(param,params.get(param));
            }
        }
        //请求块
        Request.Builder requestBuilder = new Request.Builder();
        if(headers !=null){
            for(String key:headers.keySet()){
                requestBuilder.addHeader(key,headers.get(key));
            }
        }
        Request request = requestBuilder
                .url(url)
                .post(formBodyBuilder.build())
                .build();
        //
        Call call = mOkHttpclient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed(e);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String respStr = null;
                try{
                    respStr = response.body().string();
                }catch (IOException e){
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailed(e);
                        }
                    });
                    return;
                }
                String finalRespStr = respStr;
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(finalRespStr);
                    }
                });
            }
        });
    }

    public void doPostMultiPart(String url, HashMap<String,String> headers,HashMap<String,String> params, INetCallBack callBack){

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        if(params != null){
            for(String param : params.keySet()){
                multipartBodyBuilder.addFormDataPart(param,params.get(param));
            }
        }
        Request.Builder requestBuilder=new Request.Builder();
        if(headers !=null){
            for(String key:headers.keySet()){
                requestBuilder.addHeader(key,headers.get(key));
            }
        }
        Request request = requestBuilder
                .url(url)
                .post(multipartBodyBuilder.build())
                .build();

        Call call = mOkHttpclient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed(e);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String respStr = null;
                try{
                    respStr = response.body().string();
                }catch (IOException e){
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailed(e);
                        }
                    });
                    return;
                }
                String finalRespStr = respStr;
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(finalRespStr);
                    }
                });
            }
        });
    }

    public void doPostJson(String url,HashMap<String,String> headers,String jsonStr,INetCallBack callBack){
        MediaType jsonMediaType=MediaType.get("application/json");
        RequestBody requestBody=RequestBody.create(jsonStr, jsonMediaType);

        Request.Builder requestBuilder=new Request.Builder();
        if(headers !=null){
            for(String key:headers.keySet()){
                requestBuilder.addHeader(key,headers.get(key));
            }
        }
        Request request = requestBuilder
                .url(url)
                .post(requestBody)
                .build();

        Call call = mOkHttpclient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed(e);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String respStr = null;
                try{
                    respStr = response.body().string();
                }catch (IOException e){
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailed(e);
                        }
                    });
                    return;
                }
                String finalRespStr = respStr;
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(finalRespStr);
                    }
                });
            }
        });
    }
}
