package com.zch.storeapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.zch.storeapp.R;
import com.zch.storeapp.adapter.GoodsAdapter;
import com.zch.storeapp.manager.GreenDaoManager;
import com.zch.storeapp.model.GoodsModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GoodsAdapter adapter;
    private RecyclerView mRv;
    private GreenDaoManager mDbManager;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyAdapter(mDbManager.queryGoods());
    }

    private void init() {
        mDbManager = new GreenDaoManager(this);
        initView();
    }

    private void initView() {
        mRv = findViewById(R.id.id_rv);
        adapter = new GoodsAdapter(this);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(adapter);

    }

    /**
     * 添加商品
     * @param v
     */
    public void onAddGoodsClick(View v){
        mDbManager.insertGoods();
    }

    /**
     * 查询商品
     * @param v
     */
    public void onQueryAllClick(View v){
        List<GoodsModel> datas = mDbManager.queryGoods();
        notifyAdapter(datas);
    }

    /**
     * 筛选-水果
     * @param v
     */
    public void onQueryFruitsClick(View v){
        List<GoodsModel> datas = mDbManager.queryFruits();
        notifyAdapter(datas);
    }

    /**
     * 筛选-零食
     * @param v
     */
    public void onQuerySnacksClick(View v){
        List<GoodsModel> datas = mDbManager.querySnacks();
        notifyAdapter(datas);
    }

    private void notifyAdapter(List<GoodsModel> datas){
        adapter.setDatas(datas);
    }

}
