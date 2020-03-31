package com.zchhh.servicesolution;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {

    private Context context;
    private static final String TAG = "MyService";

    MyBinder binder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        //使用前台服务
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setContentText("This is a notification");
//        builder.setContentTitle("Notification:");
//        builder.setWhen(System.currentTimeMillis());
//        Notification notification = builder.build();
//        startForeground(1,notification);
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStart: ");
        //AlarmManger实例
//        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        //定义任务触发时间为10秒后，
//        long time = (SystemClock.elapsedRealtime()+10*1000);
//        Intent i = new Intent(this,MyBroadcast.class);
//        //指定处理定时任务的广播接收器为MyReceiver
//        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
//        //调用set()方法完成设定。
//        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,time,pi);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        //下载功能实例
        //2.返回刚定义好的MyBinder类。
        return binder;
    }

    //1.定义自定义binder类
    class MyBinder extends Binder{

        void createProgressDialog(){

            Log.i("MyBinder", "createProgressDialog: ");
        }

        void onProgressUpdate(){
            Log.i("MyBinder", "onProgressUpdate: ");
        }
    }
}
