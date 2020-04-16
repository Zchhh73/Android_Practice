package com.zch.restapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zch.restapp.R;
import com.zch.restapp.bean.Order;
import com.zch.restapp.config.Config;
import com.zch.restapp.ui.activity.OrderDetailActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderItemViewHolder> {

    private List<Order> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;

    public OrderAdapter(Context context,List<Order> datas){
        mContext = context;
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.item_order_list,parent,false);
        return new OrderItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        Order order = mDatas.get(position);
        Picasso.with(mContext)
                .load(Config.baseUrl + order.getRestaurant().getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(holder.mIvImage);
        holder.mTvName.setText(order.getRestaurant().getName());
        if(order.getPs().size()>0){
            holder.mTvLabel.setText(order.getPs().get(0)
                    .product.getName()+"等"+order.getCount()+"件商品");
        }else{
            holder.mTvLabel.setText("无消费");
        }


        holder.mTvPrice.setText("共消费："+order.getPrice()+"元");
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder{

        public ImageView mIvImage;
        public TextView mTvName;
        public TextView mTvLabel;
        public TextView mTvPrice;



        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderDetailActivity.launch(mContext,mDatas.get(getLayoutPosition()));
                }
            });

            mIvImage = itemView.findViewById(R.id.id_iv_image);
            mTvName = itemView.findViewById(R.id.id_tv_name);
            mTvLabel = itemView.findViewById(R.id.id_tv_label);
            mTvPrice = itemView.findViewById(R.id.id_tv_price);

        }
    }
}
