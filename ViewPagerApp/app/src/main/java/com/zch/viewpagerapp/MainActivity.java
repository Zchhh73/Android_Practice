package com.zch.viewpagerapp;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final float MIN_SCALE=0.9F;

    private List<Integer> pics;
    private MyViewPager pagers;

    private int currentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pagers=findViewById(R.id.pagers);
        initData();





    }

    private void initData() {
        pics=new ArrayList<>();
        pics.add(R.mipmap.bg1);
        pics.add(R.mipmap.bg2);
        pics.add(R.mipmap.bg3);
        pics.add(R.mipmap.bg4);

        //实例化适配器
        PagerAdapter adapter=new MyAdapter(this, pics);
        //找到viewPager，设置适配器
        pagers.setAdapter(adapter);
        //设置当前选中item
        currentPosition=getStartItem();
        pagers.setCurrentItem(currentPosition);
    }


    public int getStartItem() {
        if (getRealCount() == 0) {
            return 0;
        }
        //设置当前选中的位置为Integer.MAX_VALUE / 2,这样开始就能往左滑动
        //但要保证与size的余数为0，这样才从第一页开始显示。
        int currentItem=getRealCount() * MyAdapter.mLooperCount / 2;
        if (currentItem % getRealCount() == 0) {
            return currentItem;

        }
        while(currentItem % getRealCount() != 0){
            currentItem++;
        }
        return currentItem;
    }

    /**
     * 返回pics长度
     *
     * @return
     */
    private int getRealCount() {
        return pics == null ? 0 : pics.size();
    }
}
