package com.zch.recyclerviewapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private myAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView=findViewById(R.id.recycler_view);
        //线性布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        //横向排列，反向展示
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        linearLayoutManager.setReverseLayout(true);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter=new myAdapter(this,mRecyclerView);

        //itemview点击事件
        adapter.setOnItemClickListener(new myAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, "第"+position+"条数据被点击", Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 添加数据
     *
     * @param v
     */
    public void onAddDataClick(View v) {
        List<String> data=new ArrayList<>();
        for (int i=0; i < 20; i++) {
            String s="第" + i + "条数据";
            data.add(s);
        }
        adapter.setDataSource(data);
    }


    /**
     * 切换布局
     *
     * @param v
     */
    public void onChangeLayout(View v) {
        //从线性布局切换网格布局
        if (mRecyclerView.getLayoutManager().getClass() == LinearLayoutManager.class) {
            //网格
            GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else if (mRecyclerView.getLayoutManager().getClass() == GridLayoutManager.class) {
            //瀑布流
            StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        } else {
            //线性
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    /**
     * 插入一条数据
     * @param v
     */
    public void onInsertDataClick(View v){
        adapter.addData(1);
    }

    /**
     * 删除一条数据
     * @param v
     */
    public void onRemoveDataClick(View v){
        adapter.removeData(1);
    }
}
