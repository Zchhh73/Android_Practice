package com.zch.expandablelistview.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {

    public static String doGet(String urlStr) {
        HttpURLConnection conn=null;
        InputStream is=null;
        ByteArrayOutputStream baos=null;
        try {
            URL url=new URL(urlStr);
            conn=(HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is=conn.getInputStream();
                baos=new ByteArrayOutputStream();
                int len=-1;
                byte[] buf=new byte[512];
                while ((len=is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
            }

            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
            conn.disconnect();
        }
        return null;
    }
}


