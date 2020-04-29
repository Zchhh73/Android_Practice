package com.zch.butterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {

    @BindView(R.id.id_recycle)
    RecyclerView mRecyclerView;
    private List<String> mData = new ArrayList<>(Arrays.asList("Simple Use","RecycleView Use"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RvAdapter(this,mData));

    }
}
