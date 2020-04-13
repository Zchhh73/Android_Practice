package com.zch.storeapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zch.storeapp.R;
import com.zch.storeapp.manager.GreenDaoManager;
import com.zch.storeapp.model.GoodsModel;

public class GoodsDetailActivity extends AppCompatActivity {

    private EditText mEtInfo;
    private GoodsModel goodsModel;
    private GreenDaoManager mDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);

        mDbManager = new GreenDaoManager(this);
        goodsModel = getIntent().getParcelableExtra("goodsModel");

        mEtInfo = findViewById(R.id.et_info);
        mEtInfo.setText(goodsModel.getInfo());

    }


    /**
     * 保存编辑点击事件
     * @param v
     */
    public void onEditClick(View v){
        String info = mEtInfo.getText().toString();
        goodsModel.setInfo(info);
        mDbManager.updateGoodsInfo(goodsModel);
        onBackPressed();
    }

    /**
     * 删除商品
     * @param v
     */
    public void onDelClick(View v){
        String info = mEtInfo.getText().toString();
        goodsModel.setInfo(info);
        mDbManager.deleteGoodsInfo(goodsModel);
        onBackPressed();
    }
}
