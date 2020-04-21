package com.zch.listviewapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleActivity extends AppCompatActivity {

    private List<Map<String, Object>> data=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        ListView listView2=findViewById(R.id.list_view);


        //实例化适配器对象
        //数据源
        initData();
        //参数4:数据来源的key数组，参数5：数据去向的id数组
        String[] from={"img", "name", "mood"};
        int[] to={R.id.qq_img, R.id.qq_name, R.id.qq_mode};
        // 参数45对应索引，from数组的元素代表数据源每个map的key，
        // key指代的数据作为to数组对应索引上id所代表的控件内容显示出来。
        SimpleAdapter adapter=new SimpleAdapter(this, data, R.layout.item3, from, to);


        //为listview设置适配器
        listView2.setAdapter(adapter);

        //点击事件
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
                Map<String, Object> map=data.get(position);
                String name=map.get("name").toString();
                String mood=map.get("mood").toString();
                Toast.makeText(SimpleActivity.this, name + ":"+mood, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initData() {
        //数据内容
        //左边：头像，右上：名字，右下：心情
        Map<String, Object> map1=new HashMap<>();
        map1.put("img", R.mipmap.caocao);
        map1.put("name", "曹操");
        map1.put("mood", "说曹操曹操到");

        Map<String, Object> map2=new HashMap<>();
        map2.put("img", R.mipmap.zhenji);
        map2.put("name", "甄姬");
        map2.put("mood", "黑牌！");

        Map<String, Object> map3=new HashMap<>();
        map3.put("img", R.mipmap.guojia);
        map3.put("name", "郭嘉");
        map3.put("mood", "抽牌");

        Map<String, Object> map4=new HashMap<>();
        map4.put("img", R.mipmap.simayi);
        map4.put("name", "司马懿");
        map4.put("mood", "判定");

        Map<String, Object> map5=new HashMap<>();
        map5.put("img", R.mipmap.caocao);
        map5.put("name", "曹操");
        map5.put("mood", "说曹操曹操到");

        Map<String, Object> map6=new HashMap<>();
        map6.put("img", R.mipmap.zhenji);
        map6.put("name", "甄姬");
        map6.put("mood", "黑牌！");

        Map<String, Object> map7=new HashMap<>();
        map7.put("img", R.mipmap.guojia);
        map7.put("name", "郭嘉");
        map7.put("mood", "抽牌");

        Map<String, Object> map8=new HashMap<>();
        map8.put("img", R.mipmap.simayi);
        map8.put("name", "司马懿");
        map8.put("mood", "判定");

        data.add(map1);
        data.add(map2);
        data.add(map3);
        data.add(map4);
        data.add(map5);
        data.add(map6);
        data.add(map7);
        data.add(map8);

    }
}
