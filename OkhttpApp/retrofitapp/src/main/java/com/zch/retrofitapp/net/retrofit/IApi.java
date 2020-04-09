package com.zch.retrofitapp.net.retrofit;

import com.zch.retrofitapp.net.retrofit.bean.Result;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface IApi {

    @GET("getmethod")
    Call<Result> get(@Header("author") String author,@Query("username") String username);

    @POST("postmethod")
    @FormUrlEncoded
    Call<Result> post(@Header("author") String author,@Field("username") String username);

    @POST("postmethod")
    @Multipart
    Call<Result> postMultipart(@Header("author") String author,@Part("username") RequestBody username);


    @POST("postjson")
    Call<Result> postJson(@Header("author") String author,@Body RequestBody jsonBody);
}
