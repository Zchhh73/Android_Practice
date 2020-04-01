package com.zch.fragmenttest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Fragment3 extends Fragment {

    //Fragment向Activity传值（接口回调）
    //1.定义一个接口，在该接口中声明用于传递数据的方法。
    //2.让Activity实现该接口，重写回调方法，获取传入的值进行处理。
    //3.在自定义Fragmt中，声明一个回调接口的引用。
    //4.在onAttach方法中，为3的引用赋值
    //5.用引用调用方法。



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment3, container, false);
    }

    private MyListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (MyListener) getActivity();
        listener.sendMsg("消息");
    }

    public interface MyListener{
        public void sendMsg(String msg);
    }

}
