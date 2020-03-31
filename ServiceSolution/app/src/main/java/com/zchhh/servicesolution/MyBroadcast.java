package com.zchhh.servicesolution;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //利用Intent对象去启动MyService这个服务
        /*
            目的：一旦启动MyService，就会在onStartCommand()方法里设定一个定时任务。
                  10秒后onReceive()会执行，紧接着又启动MyService。
                  实现了一个能长期在后台进行定时任务的服务。
         */
        Intent i = new Intent(context,MyService.class);
        context.startService(i);
    }
}
