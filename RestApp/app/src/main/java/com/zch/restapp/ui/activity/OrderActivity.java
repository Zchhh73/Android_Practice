package com.zch.restapp.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zch.restapp.R;
import com.zch.restapp.UserInfoHolder;
import com.zch.restapp.bean.Order;
import com.zch.restapp.bean.User;
import com.zch.restapp.biz.OrderBiz;
import com.zch.restapp.net.CommonCallback;
import com.zch.restapp.ui.adapter.OrderAdapter;
import com.zch.restapp.ui.view.CircleTransform;
import com.zch.restapp.ui.view.refresh.SwipeRefresh;
import com.zch.restapp.ui.view.refresh.SwipeRefreshLayout;
import com.zch.restapp.utils.T;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends BaseActivity {

    private Button mBtnOrder;
    private Button mBtnExit;
    private TextView mTvUsername;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mIvIcon;

    private RecyclerView mRecyclerView;
    private OrderAdapter adapter;
    private List<Order> mDatas = new ArrayList<>();

    private OrderBiz mOrderBiz = new OrderBiz();

    //上拉参数
    private int mCurrentPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initView();
        initEvent();
        loadDatas();


    }

    private void initEvent() {
        //点餐按钮
        mBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this,ProductListActivity.class);
                startActivityForResult(intent,1001);
            }
        });

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

        mBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitActivity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001 && resultCode==RESULT_OK){
            loadDatas();
        }

    }

    //保持登录
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            try{
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                return true;
            }catch (Exception e){

            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private void loadMore() {
        startLoadingProgress();
        mOrderBiz.listByPage(++mCurrentPage, new CommonCallback<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mCurrentPage--;
                mSwipeRefreshLayout.setPullUpRefreshing(false);
                if("用户未登录".equals(e.getMessage())){
                    toLoginActivity();
                }
            }

            @Override
            public void onResponse(List<Order> response) {
                stopLoadingProgress();
                if(response.size()==0){
                    T.showToast("没有订单了~");
                    mSwipeRefreshLayout.setPullUpRefreshing(false);
                    return;
                }else{
                    T.showToast("订单加载成功~");
                    mDatas.addAll(response);
                    adapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setPullUpRefreshing(false);
                }

            }
        });
    }

    private void loadDatas() {

        startLoadingProgress();

        mOrderBiz.listByPage(0, new CommonCallback<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);

                if("用户未登录".equals(e.getMessage())){
                    toLoginActivity();
                }
            }

            @Override
            public void onResponse(List<Order> response) {
                stopLoadingProgress();
                mCurrentPage = 0;
                T.showToast("订单更新成功");
                mDatas.clear();
                mDatas.addAll(response);
                adapter.notifyDataSetChanged();
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }


    private void initView() {
        mBtnOrder = (Button)findViewById(R.id.id_btn_order);
        mBtnExit = findViewById(R.id.id_btn_exit);
        mTvUsername = (TextView)findViewById(R.id.id_tv_username);
        mRecyclerView = findViewById(R.id.id_recycleview);
        mSwipeRefreshLayout = findViewById(R.id.id_swiperefresh);
        mIvIcon = findViewById(R.id.id_iv_icon);

        User user =UserInfoHolder.getInstance().getUser();
        if(user !=null){
            mTvUsername.setText(user.getUsername());
        }else{
            toLoginActivity();
            finish();
            return;
        }

        mSwipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.GREEN,Color.YELLOW);

        //recyclerview
        adapter = new OrderAdapter(this,mDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        Picasso.with(this)
                .load(R.drawable.icon)
                .placeholder(R.drawable.pictures_no)
                .transform(new CircleTransform())
                .into(mIvIcon);
    }

    private void exitActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("你真的要退出么？");
        builder.setCancelable(true);
        builder.setIcon(R.drawable.ic_close_red);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(OrderActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrderBiz.onDestroy();
    }
}
