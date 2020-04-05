package com.zch.asyncapp;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 1. download方法 url localPath listener
 * 2. listener: start,success, fail progress
 * 3. 用async封装
 */
public class DownloadHelper {

    public static void download(String url, String localpath, onDownloadListener listener) {
        DownloadAsyncTask task=new DownloadAsyncTask(url, localpath, listener);
        task.execute();


    }

    public static class DownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {

        String url;
        String mFilePath;
        onDownloadListener mListener;

        public DownloadAsyncTask(String url, String mFilePath, onDownloadListener mListener) {
            this.url=url;
            this.mFilePath=mFilePath;
            this.mListener=mListener;
        }

        /**
         * 在异步任务之前，在主线程
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //操作UI,准备工作
            if (mListener != null) {
                mListener.onStart();
            }


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
            String apkUrl=url;
            try {
                URL url=new URL(apkUrl);
                //构造连接打开
                URLConnection conn=url.openConnection();
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream inputStream=conn.getInputStream();
                //获得下载内容总长度
                int contentLength=conn.getContentLength();

                //下载地址处理
                File apkFile=new File(mFilePath);
                if (apkFile.exists()) {
                    boolean result=apkFile.delete();
                    if (!result) {
                        if (mListener != null) {
                            mListener.onFail(-1, apkFile, "文件删除失败");
                        }
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
                if (mListener != null) {
                    mListener.onFail(-2, new File(mFilePath), e.getMessage());
                }
                return false;
            }
            if (mListener != null) {
                mListener.onSuccess(0, new File(mFilePath));
            }

            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
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
                if (mListener != null) {
                    mListener.onProgress(values[0]);
                }
            }
        }
    }


    public interface onDownloadListener {
        void onStart();

        void onSuccess(int code, File file);

        void onFail(int code, File file, String message);

        void onProgress(int progress);
    }

}
