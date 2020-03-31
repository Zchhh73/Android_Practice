package com.zchhh.activitylife;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*
         resource:布局文件。
         root：加载layout的父ViewGroup。
         attachGroup：false表示不返回父ViewGroup。
         */
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        Button bt = (Button) view.findViewById(R.id.btn1);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "您点击了按钮", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
