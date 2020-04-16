package com.zch.restapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zch.restapp.R;
import com.zch.restapp.bean.Order;
import com.zch.restapp.bean.Product;
import com.zch.restapp.config.Config;
import com.zch.restapp.utils.T;

import java.util.List;

public class OrderDetailActivity extends BaseActivity {

    private Order mOrder;

    private ImageView mIvImage;
    private TextView mTvTitle;
    private TextView mTvDesc;
    private TextView mTvPrice;

    private static final String KEY_PRODUCT = "key_order";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        setUpToolbar();
        setTitle("订单详情");

        Intent intent = getIntent();
        if(intent != null){
            mOrder = (Order) intent.getSerializableExtra("key_order");
        }
        if(mOrder == null){
            T.showToast("参数传递错误");
            finish();
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
                .load(Config.baseUrl+mOrder.getRestaurant().getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(mIvImage);

        mTvTitle.setText("下饭食堂");

        List<Order.ProductVo> ps=mOrder.getPs();
        StringBuilder sb = new StringBuilder();
        for(Order.ProductVo productVo:ps){
            sb.append(productVo.product.getName())
                    .append(" * ")
                    .append(productVo.count)
                    .append("\n");
        }
        mTvDesc.setText(sb.toString());
        mTvPrice.setText("共消费"+mOrder.getPrice()+"元");


    }

    public static void launch(Context context, Order order){
        Intent intent = new Intent(context,OrderDetailActivity.class);
        intent.putExtra(KEY_PRODUCT,order);
        context.startActivity(intent);
    }
}
