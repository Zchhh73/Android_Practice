package com.zch.okhttpapp.net.okhttp;

public interface INetCallBack {
    void onSuccess(String response);

    void onFailed(Throwable ex);


}
