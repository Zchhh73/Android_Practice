package com.zch.cardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mLvMsgList;
    private List<Msg> mDatas=new ArrayList<>();
    private MsgAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLvMsgList=findViewById(R.id.id_lv_msgList);

        mDatas.addAll(MsgLab.generateMockList());
        mDatas.addAll(MsgLab.generateMockList());

        adapter=new MsgAdapter(this, mDatas);
        mLvMsgList.setAdapter(adapter);

    }
}
