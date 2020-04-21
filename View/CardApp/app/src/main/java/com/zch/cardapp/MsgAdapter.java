package com.zch.cardapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MsgAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Msg> mDatas;

    public MsgAdapter(Context context,List<Msg> datas){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;

    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Msg getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView==null){
            convertView = mInflater.inflate(R.layout.item_msg,parent,false);
            holder = new ViewHolder();
            holder.mIvImg = convertView.findViewById(R.id.id_iv_img);
            holder.mTvTitle = convertView.findViewById(R.id.id_tv_title);
            holder.mTvContent = convertView.findViewById(R.id.id_tv_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Msg msg = mDatas.get(position);
        holder.mIvImg.setImageResource(msg.getImgResId());
        holder.mTvTitle.setText(msg.getTitle());
        holder.mTvContent.setText(msg.getContent());

        return convertView;
    }

    public static class ViewHolder{

        ImageView mIvImg;
        TextView mTvTitle;
        TextView mTvContent;


    }
}
