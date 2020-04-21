package com.zch.viewpagerapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends PagerAdapter {
    public static final int mLooperCount = 500;

    private Context ctx;
    private List<Integer> list;

    public MyAdapter(Context ctx,List<Integer> list) {
        this.ctx = ctx;
        this.list=list;
    }

    /**
     * 返回viewpager页面的个数
     *
     * @return
     */
    @Override
    public int getCount() {
        return list.size() * mLooperCount;
    }

    /**
     * 判断是否为同一张图片
     *
     * @param view
     * @param o
     * @return
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    /**
     * viewpager滑动效果
     * @param container
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position % list.size();
        View view = View.inflate(ctx,R.layout.item,null);
        ImageView iv = view.findViewById(R.id.image_bg);
        iv.setImageResource(list.get(realPosition));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
