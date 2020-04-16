package com.zch.restapp.biz;

import com.zch.restapp.bean.Product;
import com.zch.restapp.config.Config;
import com.zch.restapp.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

/**
 * 分页加载商品
 */
public class ProductBiz {

    public void listByPage(int currentPage, CommonCallback<List<Product>> callback){
        //product_find
        //currentPage

        OkHttpUtils.post()
                .url(Config.baseUrl+"product_find")
                .addParams("currentPage",currentPage+"")
                .tag(this)
                .build()
                .execute(callback);
    }


    public void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
