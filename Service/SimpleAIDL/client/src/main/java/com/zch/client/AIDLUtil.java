package com.zch.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.zch.simpleaidl.IRemoteCallback;
import com.zch.simpleaidl.IRemoteService;
import com.zch.simpleaidl.IService;

public class AIDLUtil {
    private static volatile AIDLUtil aidlUtil;
    private IService iService;

    private IRemoteService iRemoteService;
    private IRemoteCallback iRemoteCallback;

    private AIDLUtil() {
    }

    public static AIDLUtil getInstance() {
        if (aidlUtil == null) {
            synchronized (AIDLUtil.class) {
                if (aidlUtil == null) {
                    aidlUtil=new AIDLUtil();
                }
            }
        }
        return aidlUtil;
    }

    private ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("Client", "绑定成功");
//            iService=IService.Stub.asInterface(service);
            iRemoteService = IRemoteService.Stub.asInterface(service);
            try {
                iRemoteService.Regitster(iRemoteCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iService=null;
            iRemoteService = null;
            iRemoteCallback=null;
        }
    };

    public void bindService(Context context,IRemoteCallback iRemoteCallback) {
        this.iRemoteCallback = iRemoteCallback;
        Intent intent=new Intent("com.zch.aidlTest");
        intent.setPackage("com.zch.simpleaidl");
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(Context context) {
        if (iRemoteService == null) {
            return;
        }
        try {
            if(iRemoteCallback != null){
                iRemoteService.unRegitster(iRemoteCallback);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        context.unbindService(conn);
        iRemoteService=null;
        iRemoteCallback=null;
    }

    public String request(String func, String params) {
        if (iService == null) {
            Log.i("Client", "iService为空");
            return null;
        }
        Log.i("Client", "发起AIDL请求");
        try {
            String data=iService.getData(func, params);
            return data;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void send(Context context) {
        if (null == iRemoteService) {
            return;
        }
        try {
            iRemoteService.send(context.getPackageName(), "test", "");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
