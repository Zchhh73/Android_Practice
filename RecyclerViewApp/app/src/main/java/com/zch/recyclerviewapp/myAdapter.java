package com.zch.recyclerviewapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {

    private onItemClickListener onItemClickListener;
    private RecyclerView mRv;
    private List<String> dataSource;
    private Context mContext;

    private int addDataPos = -1;

    public myAdapter(Context context,RecyclerView recyclerView){
        this.mContext = context;
        this.dataSource = new ArrayList<>();
        this.mRv = recyclerView;
    }

    public void setOnItemClickListener(myAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener=onItemClickListener;
    }

    public void setDataSource(List<String> dataSource) {
        this.dataSource=dataSource;
        notifyDataSetChanged();
    }

    /**
     * 创建返回ViewHolder
     * @param parent
     * @param position
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_layout,parent,false));

    }

    /**
     * ViewHolder绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.mIv.setImageResource(getIcon(position));
        holder.mTv.setText(dataSource.get(position));

        /**
         * 只在瀑布流中使用随机高度
         */
        if(mRv.getLayoutManager().getClass() == StaggeredGridLayoutManager.class){
            //获取控件参数（宽，高）
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getRandomHeight());
            holder.mTv.setLayoutParams(params);
        }else{
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.mTv.setLayoutParams(params);
        }

        //改变add背景颜色
        if(addDataPos == position){
            holder.mItemView.setBackgroundColor(Color.RED);
        }

        holder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用接口回调方法
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    /**
     * 返回数据数量
     * @return
     */
    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    private int getIcon(int position){
        switch (position % 5){
            case 0:
                return R.mipmap.a;
            case 1:
                return R.mipmap.b;
            case 2:
                return R.mipmap.c;
            case 3:
                return R.mipmap.d;
            case 4:
                return R.mipmap.e;
        }
        return 0;
    }

    private int getRandomHeight(){
        return (int)(Math.random()*1000);
    }

    /**
     * 添加一条数据
     * @param position
     */
    public void addData(int position){
        addDataPos = position;
        dataSource.add(position,"插入的数据");
        notifyItemInserted(position);
        //刷新itemview
        notifyItemChanged(position,dataSource.size()-position);
    }

    /**
     * 删除一个数据
     * @param position
     */
    public void removeData(int position){
        addDataPos = -1;
        dataSource.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position,dataSource.size()-position);
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        View mItemView;
        ImageView mIv;
        TextView mTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mIv = itemView.findViewById(R.id.iv);
            mTv = itemView.findViewById(R.id.tv);
            mItemView = itemView;
        }
    }

    /**
     * ItemView点击事件回调接口
     */
    interface  onItemClickListener{
        void onItemClick(int position);
    }
}
