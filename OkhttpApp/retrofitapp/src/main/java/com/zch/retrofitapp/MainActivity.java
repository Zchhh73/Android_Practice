package com.zch.retrofitapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zch.retrofitapp.net.retrofit.IApi;
import com.zch.retrofitapp.net.retrofit.RetrofitImpl;
import com.zch.retrofitapp.net.retrofit.bean.Result;

import java.io.IOException;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button mBtnGet, mBtnPost, mBtnPostMulti, mBtnPostJson;
    private TextView mTvContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvents();
    }

    private void initEvents() {
        mBtnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IApi api=RetrofitImpl.getRetrofit().create(IApi.class);
                api.get("get_zch","zch9573").enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                            Result result=response.body();
                            mTvContent.setText(result.errorCode + "," + result.errorMsg + "," +
                                    result.data.headers + "," + result.data.params);
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });


        mBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IApi api = RetrofitImpl.getRetrofit().create(IApi.class);
                api.post("get_zch","zch").enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        Result result = response.body();
                        mTvContent.setText(result.errorCode + "," + result.errorMsg + "," +
                                result.data.headers + "," + result.data.params);

                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        mBtnPostMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IApi api = RetrofitImpl.getRetrofit().create(IApi.class);
                MediaType mediaType = MediaType.get("text/plain; charset=utf-8");
                RequestBody username = RequestBody.create(mediaType,"zchhh9573");

                api.postMultipart("get_zch",username).enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        Result result = response.body();
                        mTvContent.setText(result.errorCode + "," + result.errorMsg + "," +
                                result.data.headers + "," + result.data.params);

                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mBtnPostJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IApi api=RetrofitImpl.getRetrofit().create(IApi.class);
                MediaType mediaType=MediaType.get("application/json; charset=utf-8");
                RequestBody username=RequestBody.create(mediaType, "{\"username\":\"zch9573\",\"age\":18}");
                api.postJson("get_zch",username).enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        Result result=response.body();
                        mTvContent.setText(result.errorCode + "," + result.errorMsg + "," +
                                result.data.headers + "," + result.data.json);
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void initView() {
        mBtnGet=findViewById(R.id.btn_get);
        mBtnPost=findViewById(R.id.btn_post);
        mBtnPostMulti=findViewById(R.id.btn_post_multi);
        mBtnPostJson=findViewById(R.id.btn_post_json);
        mTvContent=findViewById(R.id.tv_content);

    }
}
