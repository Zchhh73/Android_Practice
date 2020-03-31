package com.zchhh.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AnotherReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"接收到自定义的有序广播",Toast.LENGTH_LONG).show();
        abortBroadcast();
    }
}
