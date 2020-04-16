package com.zch.restapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zch.restapp.R;
import com.zch.restapp.config.Config;
import com.zch.restapp.ui.activity.ProductDetailActivity;
import com.zch.restapp.utils.T;
import com.zch.restapp.vo.ProductItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListItemViewHolder> {

    private Context mContext;
    private List<ProductItem> mProductItems;
    private LayoutInflater mInflater;

    public ProductListAdapter(Context context, List<ProductItem> datas) {
        mContext=context;
        mProductItems=datas;
        mInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ProductListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.item_product_list, parent, false);
        return new ProductListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListItemViewHolder holder, int position) {
        ProductItem productItem=mProductItems.get(position);
        Picasso.with(mContext)
                .load(Config.baseUrl+productItem.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(holder.mIvImage);

        holder.mTvName.setText(productItem.getName());
        holder.mTvCount.setText(productItem.count+"");
        holder.mTvLabel.setText(productItem.getLabel());
        holder.mTvPrice.setText(productItem.getPrice()+"元/份");
    }

    @Override
    public int getItemCount() {
        return mProductItems.size();
    }

    public interface OnProductListener {

        void onProductAdd(ProductItem productItem);

        void onProductSub(ProductItem productItem);
    }

    private OnProductListener mOnProductListener;

    public void setOnProductListener(OnProductListener onProductListener) {
        mOnProductListener=onProductListener;
    }


    class ProductListItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView mIvImage;
        public TextView mTvName;
        public TextView mTvLabel;
        public TextView mTvPrice;

        public ImageView mIvAdd;
        public ImageView mIvSub;
        public TextView mTvCount;


        public ProductListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            mIvImage=itemView.findViewById(R.id.id_iv_image);
            mTvName=itemView.findViewById(R.id.id_tv_name);
            mTvLabel=itemView.findViewById(R.id.id_tv_label);
            mTvPrice=itemView.findViewById(R.id.id_tv_price);
            mIvAdd=itemView.findViewById(R.id.id_iv_add);
            mIvSub=itemView.findViewById(R.id.id_iv_sub);
            mTvCount=itemView.findViewById(R.id.id_tv_count_prod);

            //todo 跳转商品详情页
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductDetailActivity.launch(mContext,mProductItems.get(getLayoutPosition()));
                }
            });

            mIvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getLayoutPosition();
                    ProductItem productItem=mProductItems.get(pos);
                    productItem.count+=1;
                    mTvCount.setText(productItem.count + "");
                    //回调到activity
                    if (mOnProductListener != null) {
                        mOnProductListener.onProductAdd(productItem);
                    }

                }
            });


            mIvSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getLayoutPosition();
                    ProductItem productItem=mProductItems.get(pos);

                    if (productItem.count <= 0) {
                        T.showToast("已经是0了.买一些吧~");
                        return;
                    }
                    productItem.count-=1;
                    mTvCount.setText(productItem.count + "");
                    //回调到activity
                    if (mOnProductListener != null) {
                        mOnProductListener.onProductSub(productItem);
                    }
                }
            });

        }
    }
}
