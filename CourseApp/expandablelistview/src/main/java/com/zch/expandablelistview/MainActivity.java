package com.zch.expandablelistview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.zch.expandablelistview.adapter.ChapterAdapter;
import com.zch.expandablelistview.bean.Chapter;
import com.zch.expandablelistview.bean.ChapterLab;
import com.zch.expandablelistview.biz.ChapterBiz;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG="MainActivity";
    private ExpandableListView mExpandableListView;
    private BaseExpandableListAdapter mAdapter;
    private List<Chapter> mDatas=new ArrayList<>();

    private ChapterBiz mChapterBiz = new ChapterBiz();

    private Button mBtnRefresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initEvents();
        
        loadDatas(true);
        

    }

    private void loadDatas(boolean useCache) {
        mChapterBiz.loadDatas(this, new ChapterBiz.CallBack() {
            @Override
            public void onSuccess(List<Chapter> chapterList) {
                mDatas.clear();
                mDatas.addAll(chapterList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception ex) {
                ex.printStackTrace();
            }
        },useCache);


    }

    private void initEvents() {
        /*
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d(TAG, "onChildClick groupPosition=" + groupPosition + ",childPosition=" + childPosition);
                return false;
            }
        });

        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d(TAG, "OnGroupClick groupPosition=" + groupPosition );

                return false;
            }
        });

        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.d(TAG, "OnGroupCollapse groupPosition=" + groupPosition );

            }
        });

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.d(TAG, "OnGroupExpand groupPosition=" + groupPosition );
            }
        });

         */
        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDatas(false);
            }
        });

    }

    private void initViews() {
        mBtnRefresh = findViewById(R.id.id_btn_refresh);
        mExpandableListView=findViewById(R.id.expandableListView);
        mDatas.clear();
//        mDatas.addAll(ChapterLab.generateMockDatas());
        mAdapter=new ChapterAdapter(this, mDatas);
        mExpandableListView.setAdapter(mAdapter);

    }


}
