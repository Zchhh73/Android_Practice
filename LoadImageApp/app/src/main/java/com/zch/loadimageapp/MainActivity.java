package com.zch.loadimageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView mIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIv=findViewById(R.id.iv);
    }

    public void onLoadImageClick(View v) {
//        loadUrlImage("https://www.imooc.com/static/img/index/logo.png");
//        loadUrlImage("http://res.lgdsunday.club/big_img.jpg");
        glideLoadImage("http://res.lgdsunday.club/big_img.jpg");
    }

    /**
     * 加载网络图片
     *
     * @param img 地址
     */
    private void loadUrlImage(final String img) {

        mIv.setImageResource(R.mipmap.loading);
        /**
         * 1.找到地址
         * 2.根据图片地址，把图片转化为被加载的对象
         * 3.通过imageView展示
         */

        new Thread() {
            @Override
            public void run() {
                super.run();
                Message message=new Message();
                try {
                    URL url=new URL(img);
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    //请求成功
                    int code=conn.getResponseCode();
                    if (code == 200) {
                        //获取数据流
                        InputStream is=conn.getInputStream();
                        //构建Bitmap对象,可被ImageView加载的对象
                        Bitmap bitmap=BitmapFactory.decodeStream(is);
                        message.obj=bitmap;
                        message.what=200;
                    } else {
                        message.what=code;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what = -1;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what = -1;
                }finally {
                    mHandler.sendMessage(message);
                }

            }
        }.start();

    }

    private Handler mHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    //获取对象
                    Bitmap bitmap = (Bitmap) msg.obj;
                    mIv.setImageBitmap(bitmap);
                    break;
                default:
                    mIv.setImageResource(R.mipmap.loader_error);
                    break;
            }
        }
    };

    /**
     * 通过glide加载
     * @param img
     */
    private void glideLoadImage(String img){
        //配置Glide
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.loading)
                .error(R.mipmap.loader_error)
                .circleCrop();

        /**
         * 1.with()-创建图片加载实例
         * 2.load()-指定加载的图片资源
         * 3.into（）-指定图片的加载控件
         * 4.apply()-加载配置
         */
        Glide.with(this)
                .load(img)
                //封装glide配置
                .apply(GlideOptionsUtils.circleCropOptions())
                .into(mIv);
    }

}
