// IRemoteService.aidl
package com.zch.simpleaidl;
import com.zch.simpleaidl.IRemoteCallback;
// Declare any non-default types here with import statements

interface IRemoteService {
    void Regitster(IRemoteCallback callback);
    void unRegitster(IRemoteCallback callback);
    void send(String packageName,String func,String params);


}
