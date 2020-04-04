package com.zch.imageapp;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetPicThread implements Runnable {

    private String path;
    private Handler handler;

    public GetPicThread() {
    }

    public GetPicThread(String path, Handler handler) {
        this.path=path;
        this.handler=handler;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(path);
            InputStream is = null;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                is = conn.getInputStream();
                Bitmap bm =BitmapFactory.decodeStream(is);

                Message msg = new Message();
                msg.what = 100;
                msg.obj = bm;
                handler.sendMessage(msg);
            }
            assert is!=null;
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
