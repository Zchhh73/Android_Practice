package com.zch.butterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {


    private LayoutInflater mLayoutInflater;
    private List<String> mDatas;

    public RvAdapter(Context context, List<String> datas){
        mLayoutInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RvAdapter.ViewHolder(mLayoutInflater.inflate(R.layout.item_category,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextView.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.id_title_tv)
        TextView mTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
