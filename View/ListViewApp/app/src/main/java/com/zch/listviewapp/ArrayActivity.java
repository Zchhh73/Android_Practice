package com.zch.listviewapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 1.准备布局（每一项显示效果）
 * 2.准备数据源
 * 3.实例化适配器（布局+数据源）
 * 4.为ListView设置适配器
 */
public class ArrayActivity extends AppCompatActivity {
    private ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_array);


        listView1=findViewById(R.id.listview1);


        String[] data = {"AA","BB","CC","DD","EE","FF","AA","BB","CC","DD","EE","FF","AA","BB","CC","DD","EE","FF"};
        

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,data);

        listView1.setAdapter(adapter);

    }
}
