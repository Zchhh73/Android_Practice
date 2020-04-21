package com.zch.listviewapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<Msg> list;
    private Context ctx;

    public MyAdapter(List<Msg> list, Context ctx) {
        this.list=list;
        this.ctx=ctx;
    }

    //获取数量（设置listview长度）
    @Override
    public int getCount() {
        return list.size();
    }

    //获取子项
    @Override
    public Object getItem(int position) {
        return null;
    }


    //获取子项Id
    @Override
    public long getItemId(int position) {
        return 0;
    }

    //获取视图（设置listview显示效果）,每个视图出现前都会执行。
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //对View设置
        //将布局资源转为view

        ViewHolder holder;
        if (convertView == null) {
            //优化1：利用进入RecycleBin中的view，减少读view的赋值
            convertView=LayoutInflater.from(ctx).inflate(R.layout.item4, null);
            holder = new ViewHolder();
            holder.profile =convertView.findViewById(R.id.profile);
            holder.nickname=convertView.findViewById(R.id.nickname);
            holder.content=convertView.findViewById(R.id.content);
            holder.like=convertView.findViewById(R.id.like);
            holder.comment=convertView.findViewById(R.id.comment);
            holder.repost=convertView.findViewById(R.id.repost);
            //绑定
            convertView.setTag(holder);
        }else{
            //通过getTag()取出viewholder对象，通过holder.控件方式在外面直接操作控件
            holder = (ViewHolder) convertView.getTag();
        }

        Msg m=list.get(position);
        //头像
//        ImageView profile=convertView.findViewById(R.id.profile);
        holder.profile.setImageResource(m.getProfile());
        //昵称
//        TextView nickname=convertView.findViewById(R.id.nickname);
        holder.nickname.setText(m.getNickname());
        //内容
//        TextView content=convertView.findViewById(R.id.content);
        holder.nickname.setText(m.getContent());
        //点赞
//        ImageView IsLike=convertView.findViewById(R.id.like);
        if (m.isLike()) {
            holder.like.setImageResource(R.mipmap.liked);
        } else {
            holder.like.setImageResource(R.mipmap.like);
        }
        //评论
//        ImageView comment=convertView.findViewById(R.id.comment);
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "你点击评论", Toast.LENGTH_SHORT).show();
            }
        });

        //转发
//        ImageView repost=convertView.findViewById(R.id.repost);
        holder.repost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "你点击了转发", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    //1.自定义一个类，叫做ViewHolder
    //2.将需要保存的视图声明为公开属性
    //3.什么时候保存? 当view为null时，完成对viewHolder的实例化工作，并为属性赋值
    //4.什么时候用? 性能提升在view不为null时体现
    //5.怎么用? 当view为null是，viewholder初始化工作后，调用view.setTag(holder);
    //          当view不为null时，holder = view.getTag()
    static class ViewHolder {
        public ImageView profile, like, comment, repost;
        public TextView nickname, content;
    }
}
