package com.zch.restapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zch.restapp.R;
import com.zch.restapp.bean.Order;
import com.zch.restapp.bean.Product;
import com.zch.restapp.biz.OrderBiz;
import com.zch.restapp.biz.ProductBiz;
import com.zch.restapp.net.CommonCallback;
import com.zch.restapp.ui.adapter.ProductListAdapter;
import com.zch.restapp.ui.view.refresh.SwipeRefresh;
import com.zch.restapp.ui.view.refresh.SwipeRefreshLayout;
import com.zch.restapp.utils.T;
import com.zch.restapp.vo.ProductItem;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends BaseActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTvCount;
    private Button mBtnPay;

    private RecyclerView mRecyclerView;
    private ProductListAdapter adapter;
    private List<ProductItem> mDatas=new ArrayList<>();

    private ProductBiz mProductBiz=new ProductBiz();
    private OrderBiz mOrderBiz=new OrderBiz();

    private Order mOrder=new Order();

    private int mCurrnetPage;
    private float mTotalPrice;
    private int mTotalCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setUpToolbar();
        setTitle("订单");

        initView();
        initEvent();
        loadDatas();

    }

    private void initEvent() {
        //下拉
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDatas();
            }
        });

        //上拉
        mSwipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadMore();

            }
        });

        adapter.setOnProductListener(new ProductListAdapter.OnProductListener() {
            @Override
            public void onProductAdd(ProductItem productItem) {
                mTotalCount++;
                mTvCount.setText("数量：" + mTotalCount);
                mTotalPrice+=productItem.getPrice();
                mBtnPay.setText(mTotalPrice + "元  立即支付");

                mOrder.addProduct(productItem);
            }

            @Override
            public void onProductSub(ProductItem productItem) {
                mTotalCount--;
                mTvCount.setText("数量：" + mTotalCount);
                mTotalPrice-=productItem.getPrice();

                if (mTotalCount == 0) {
                    mTotalPrice=0;
                }
                mBtnPay.setText(mTotalPrice + "元  立即支付");

                mOrder.removeProduct(productItem);
            }
        });

        //mBtnPay
        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTotalCount <= 0) {
                    T.showToast("您还没选菜哦~");
                    return;
                }
                mOrder.setCount(mTotalCount);
                mOrder.setPrice(mTotalPrice);
                mOrder.setRestaurant(mDatas.get(0).getRestaurant());


                startLoadingProgress();
                mOrderBiz.add(mOrder, new CommonCallback<String>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());

                    }

                    @Override
                    public void onResponse(String response) {
                        stopLoadingProgress();
                        T.showToast("订单支付成功~");

                        setResult(RESULT_OK);
                        finish();


                    }
                });

            }
        });
    }

    private void loadDatas() {
        startLoadingProgress();
        mProductBiz.listByPage(0, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(List<Product> response) {
                stopLoadingProgress();
                mSwipeRefreshLayout.setRefreshing(false);
                mCurrnetPage=0;
                mDatas.clear();
                T.showToast("帮你清空啦，重新选吧~");
                for (Product p : response) {
                    mDatas.add(new ProductItem(p));
                }
                adapter.notifyDataSetChanged();
                //清空选择的数据，数量，价格。
                mTotalCount=0;
                mTotalPrice=0;

                mTvCount.setText("数量：" + mTotalCount);
                mBtnPay.setText(mTotalPrice + "元  立即支付");


            }
        });
    }

    private void loadMore() {

        startLoadingProgress();
        mProductBiz.listByPage(++mCurrnetPage, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mCurrnetPage--;
                mSwipeRefreshLayout.setPullUpRefreshing(false);
            }

            @Override
            public void onResponse(List<Product> response) {
                stopLoadingProgress();
                mSwipeRefreshLayout.setPullUpRefreshing(false);

                if (response.size() == 0) {
                    T.showToast("没菜啦，这些够你选的了~");
                    return;
                }
                T.showToast("又找到" + response.size() + "道菜了");
                for (Product p : response) {
                    mDatas.add(new ProductItem(p));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void initView() {
        mSwipeRefreshLayout=findViewById(R.id.id_swipe_prod);
        mRecyclerView=findViewById(R.id.id_recy_prod);
        mBtnPay=findViewById(R.id.id_btn_pay);
        mTvCount=findViewById(R.id.id_tv_count);

        mSwipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.YELLOW);

        adapter=new ProductListAdapter(this, mDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProductBiz.onDestroy();
    }
}
