package com.zch.loadimageapp;

import com.bumptech.glide.request.RequestOptions;

public class GlideOptionsUtils {

    public static RequestOptions baseOptions(){

        return new RequestOptions()
                .placeholder(R.mipmap.loading)
                .error(R.mipmap.loader_error);
    }


    public static RequestOptions circleCropOptions(){
        return baseOptions().circleCrop();
    }


}
