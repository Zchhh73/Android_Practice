package com.zch.expandablelistview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zch.expandablelistview.R;
import com.zch.expandablelistview.bean.Chapter;
import com.zch.expandablelistview.bean.ChapterItem;

import java.util.List;

public class ChapterAdapter extends BaseExpandableListAdapter {

    private List<Chapter> mDatas;
    private LayoutInflater mInflater;
    private Context mContext;

    public ChapterAdapter(Context context, List<Chapter> datas) {
        mContext=context;
        mDatas=datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return mDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDatas.get(groupPosition).getChildren().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDatas.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ParentViewHolder parentViewHolder=null;

        if (convertView == null) {
            convertView= mInflater.inflate(R.layout.item_parent_chapter,parent,false);

            parentViewHolder = new ParentViewHolder();
            parentViewHolder.mTvName = convertView.findViewById(R.id.id_tv_pname);
            parentViewHolder.mIvIndicator = convertView.findViewById(R.id.id_iv_indicator);
            convertView.setTag(parentViewHolder);
        } else {
            parentViewHolder = (ParentViewHolder)convertView.getTag();
        }

        Chapter chapter = mDatas.get(groupPosition);;
        parentViewHolder.mTvName.setText(chapter.getName());
        parentViewHolder.mIvIndicator.setImageResource(R.drawable.group_indicator);
        parentViewHolder.mIvIndicator.setSelected(isExpanded);



        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if(convertView==null){
            convertView = mInflater.inflate(R.layout.item_child_chapter,parent,false);

            childViewHolder = new ChildViewHolder();
            childViewHolder.mTvName = convertView.findViewById(R.id.id_tv_name);
            convertView.setTag(childViewHolder);
        }else{
            childViewHolder = (ChildViewHolder)convertView.getTag();
        }

        ChapterItem chapterItem = mDatas.get(groupPosition).getChildren().get(childPosition);
        childViewHolder.mTvName.setText(chapterItem.getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class ParentViewHolder {
        TextView mTvName;
        ImageView mIvIndicator;
    }

    public static class ChildViewHolder {
        TextView mTvName;
    }


}
