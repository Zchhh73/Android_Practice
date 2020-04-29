package com.zch.butterapp;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    @BindView(R.id.id_lv)
    ListView mListView;

    private List<String> mData = new ArrayList<>(Arrays.asList("Simple Use","RecycleView Use"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ButterKnife.bind(this);

        mListView.setAdapter(new CategoryAdapter(this,mData));
    }

    @OnItemClick(R.id.id_lv)
    public void itemClick(int position){
        Intent intent = null;
        switch (position){
            case 0:
                intent = new Intent(this,MainActivity.class);
                break;
            case 1:
                intent = new Intent(this,RecyclerActivity.class);
                break;
        }
        if(intent != null){
            startActivity(intent);
        }

    }
}
