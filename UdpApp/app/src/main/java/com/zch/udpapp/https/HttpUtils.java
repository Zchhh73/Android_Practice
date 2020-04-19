package com.zch.udpapp.https;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.zch.udpapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;


public class HttpUtils {

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public interface HttpListener{
        void onSuccess(String content);
        void onFail(Exception e);

    }

    public static void doGet(final Context context, final String urlstr, final HttpListener listener){
        new Thread(){
            @Override
            public void run() {
                InputStream is = null;
                try {
                    URL url = new URL(urlstr);
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                    //证书校验

                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    //信任的数组对象
                    X509Certificate serverCert = getCert(context);
                    TrustManager[] trustManagers = {new MyX509TrustManager(serverCert)};
                    //初始化
                    sslContext.init(null,trustManagers,new SecureRandom());
                    conn.setSSLSocketFactory(sslContext.getSocketFactory());

                    //校验域名
                    conn.setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {

                            //验证域名对象
                            HostnameVerifier defaultHostnameVerifier=HttpsURLConnection.getDefaultHostnameVerifier();

                            //证书中的名称
                            return defaultHostnameVerifier.verify("kyfw.12306.cn",session);
                        }
                    });

                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    conn.connect();

                    is=conn.getInputStream();

                    InputStreamReader isr = new InputStreamReader(is);
                    char[] buf = new char[2048];
                    int len = -1;
                    final StringBuilder content = new StringBuilder();
                    while((len=isr.read(buf))!=-1){
                        content.append(new String(buf,0,len));
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(content.toString());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFail(e);
                }finally{
                    try {
                        if(is != null){
                            is.close();
                        }
                    } catch (IOException e) {
                        //ignore
                    }
                }
            }
        }.start();
    }

    private static X509Certificate getCert(Context context) {
        try {
            InputStream open = context.getAssets().open("srca.cer");
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            return (X509Certificate)certificateFactory.generateCertificate(open);
        } catch (IOException | CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

}
