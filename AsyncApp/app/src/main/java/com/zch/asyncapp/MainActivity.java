package com.zch.asyncapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * 下载任务：
 * 1.网络上请求数据： 申请权限，读写SD卡权限
 * 2.布局
 * 3.下载前    UI处理
 * 4.下载中    数据
 * 5.下载后    UI处理
 */
public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    public static final int INIT_PROGRESS=0;
    public static final String APK_URL="http://download.sj.qq.com/upload/connAssitantDownload/upload/MobileAssistant_1.apk";
    public static final String FILE_NAME="imooc.apk";
    public static final int WRITE_READ_EXTERNAL_STORAGE=100;


    private ProgressBar progressBar;
    private Button btn_pro;
    private TextView txt_pro;


    private static final String TAG="MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setListener();
        //初始化数据
        setData();
    }


    private void initView() {
        progressBar=findViewById(R.id.progressBar);
        btn_pro=findViewById(R.id.btn_pro);
        txt_pro=findViewById(R.id.textview);
    }

    private void setListener() {

        btn_pro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkPerm();
            }
        });

    }

    private void setData() {
        txt_pro.setText(R.string.downl_txt);
        btn_pro.setText(R.string.click_download);
        progressBar.setProgress(INIT_PROGRESS);
    }

    /**
     * 动态申请权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(WRITE_READ_EXTERNAL_STORAGE)
    private void checkPerm(){
        String[] params = {Manifest.permission.READ_EXTERNAL_STORAGE,
                           Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(EasyPermissions.hasPermissions(this,params)){
            DownloadAsyncTask asyncTask=new DownloadAsyncTask();
            asyncTask.execute(APK_URL);
        }else{
            EasyPermissions.requestPermissions(this,"需要读写本地权限",WRITE_READ_EXTERNAL_STORAGE,params);
        }

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            new AppSettingsDialog.Builder(this)
                    .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置屏幕以修改应用权限")
                    .setTitle("必需权限")
                    .build().show();
        }
    }

    /**
     * String 入参
     * Integer 进度
     * Boolean 结果值
     */
    public class DownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {
        String mFilePath;

        /**
         * 在异步任务之前，在主线程
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //操作UI,准备工作
            btn_pro.setText(R.string.downloading);
            txt_pro.setText(R.string.downloading);
            progressBar.setProgress(INIT_PROGRESS);

        }


        /**
         * 在另外一个线程中处理事件
         *
         * @param strings 入参
         * @return 结果
         */
        @Override
        protected Boolean doInBackground(String... strings) {

            //下载过程
            if (strings != null && strings.length > 0) {
                String apkUrl=strings[0];
                try {
                    URL url=new URL(apkUrl);
                    //构造连接打开
                    URLConnection conn=url.openConnection();
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    InputStream inputStream=conn.getInputStream();
                    //获得下载内容总长度
                    int contentLength=conn.getContentLength();
                    //下载地址
                    mFilePath=Environment.getExternalStorageDirectory()
                            + File.separator + FILE_NAME;
                    //下载地址处理
                    File apkFile=new File(mFilePath);
                    if (apkFile.exists()) {
                        boolean result=apkFile.delete();
                        if (!result) {
                            return false;
                        }
                    }
                    //下载的大小
                    int downloadsize=0;

                    byte[] bytes=new byte[1024];
                    int length;

                    //创建输出流并下载
                    OutputStream outputStream=new FileOutputStream(mFilePath);
                    while ((length=inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, length);
                        downloadsize+=length;
                        //发送进度
                        publishProgress(downloadsize * 100 / contentLength);
                    }

                    inputStream.close();
                    outputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

            } else {
                return false;
            }
            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //在主线程中，执行结果。
            btn_pro.setText(aBoolean ? "下载完成" : "下载失败");
            txt_pro.setText(aBoolean ? "下载完成" + mFilePath : "下载失败");
        }

        /**
         * 进度变化
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //收到进度处理
            if (values != null && values.length > 0) {
                progressBar.setProgress(values[0]);
            }


        }

    }
}
