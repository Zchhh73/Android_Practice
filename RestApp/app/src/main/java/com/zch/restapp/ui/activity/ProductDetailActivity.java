package com.zch.restapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zch.restapp.R;
import com.zch.restapp.bean.Product;
import com.zch.restapp.config.Config;
import com.zch.restapp.utils.T;

public class ProductDetailActivity extends BaseActivity {

    private Product mProduct;

    private ImageView mIvImage;
    private TextView mTvTitle;
    private TextView mTvDesc;
    private TextView mTvPrice;

    private static final String KEY_PRODUCT = "key_product";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        setUpToolbar();
        setTitle("详情");

        Intent intent = getIntent();
        if(intent != null){
            mProduct = (Product) intent.getSerializableExtra("key_product");
        }
        if(mProduct == null){
            T.showToast("参数传递错误");
            return;
        }
        initView();
    }

    private void initView() {
        mIvImage = findViewById(R.id.id_iv_detail_img);
        mTvTitle = findViewById(R.id.id_tv_detail_title);
        mTvDesc = findViewById(R.id.id_tv_detail_desc);
        mTvPrice = findViewById(R.id.id_tv_detail_price);

        Picasso.with(this)
                .load(Config.baseUrl+mProduct.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(mIvImage);

        mTvTitle.setText(mProduct.getName());
        mTvDesc.setText(mProduct.getDescription());
        mTvPrice.setText(mProduct.getPrice()+"元/份");


    }

    public static void launch(Context context,Product product){
        Intent intent = new Intent(context,ProductDetailActivity.class);
        intent.putExtra(KEY_PRODUCT,product);
        context.startActivity(intent);
    }
}
