package com.zch.storeapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zch.storeapp.ui.GoodsDetailActivity;
import com.zch.storeapp.R;
import com.zch.storeapp.model.GoodsModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.VH> {

    private Context mContext;
    private List<GoodsModel> datas;

    public GoodsAdapter(Context context) {
        this.mContext=context;
    }

    public void setDatas(List<GoodsModel> datas) {
        this.datas=datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(mContext).inflate(R.layout.item_good, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final GoodsModel model = datas.get(position);
        holder.mIvIcon.setImageResource(mContext.getResources().getIdentifier(model.getIcon(),"mipmap",mContext.getPackageName()));
        holder.mTvName.setText(model.getName());
        holder.mTvInfo.setText(model.getInfo());
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("goodsModel",model);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    public static class VH extends RecyclerView.ViewHolder {

        public ImageView mIvIcon;
        public TextView mTvName, mTvInfo;

        public VH(@NonNull View itemView) {
            super(itemView);

            mIvIcon=itemView.findViewById(R.id.iv_icon);
            mTvName=itemView.findViewById(R.id.tv_name);
            mTvInfo=itemView.findViewById(R.id.tv_info);

        }
    }
}
