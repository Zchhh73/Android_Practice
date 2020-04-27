package com.zch.simpleaidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.zch.simpleaidl.IService;

import org.json.JSONException;
import org.json.JSONObject;

public class AIDLService extends Service {

    private static final String TAG="ZCH";
    public AIDLService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return stub;
    }

    IService.Stub stub = new IService.Stub() {
        @Override
        public String getData(String func, String params) throws RemoteException {
            Log.i(TAG, "接收到请求");
            Log.i(TAG, "func: "+func+"; params："+params);
            JSONObject jsonObject = new JSONObject();
            switch (func){
                case "char":
                    try {
                        jsonObject.put("name","zhangchenhan");
                        jsonObject.put("sex","man");
                        long time = System.currentTimeMillis();
                        jsonObject.put("time",time);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "":
                    break;
            }

            String s = jsonObject.toString();
            return s;
        }
    };
}
