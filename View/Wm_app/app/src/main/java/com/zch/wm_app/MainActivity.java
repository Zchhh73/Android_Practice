package com.zch.wm_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.zch.wm_app.model.Food;
import com.zch.wm_app.model.Person;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mNameText;
    private RadioGroup mSexGroup;
    private CheckBox mHotCheckBox, mFishCheckBox, mSourCheckBox;
    private SeekBar mSeekBar;
    private Button mSearchButton;
    private ImageView mFoodImageView;
    private ToggleButton mToggleButton;

    private List<Food> mfoods;
    private List<Food> foodresult;

    private Person person;
    private boolean isFish;
    private boolean isSour;
    private boolean isHot;
    private int price;

    //默认显示
    private int mCurrentIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        findViews();

        //初始化数据
        initData();

        // 添加监听器
        setListeners();

        //测试

    }

    private void initData() {
        //new列表
        mfoods=new ArrayList<>();
        mfoods.add(new Food("麻辣香锅", 60, R.drawable.malaxiangguo, true, false, false));
        mfoods.add(new Food("水煮鱼", 48, R.drawable.shuizhuyu, true, true, false));
        mfoods.add(new Food("麻辣火锅", 88, R.drawable.malahuoguo, true, true, false));
        mfoods.add(new Food("清蒸鲈鱼", 98, R.drawable.qingzhengluyu, false, false, false));
        mfoods.add(new Food("桂林米粉", 16, R.drawable.guilin, false, false, true));
        mfoods.add(new Food("上汤娃娃菜", 20, R.drawable.wawacai, false, false, false));
        mfoods.add(new Food("红烧肉", 30, R.drawable.hongshaorou, false, false, false));
        mfoods.add(new Food("木须肉", 18, R.drawable.muxurou, false, false, false));
        mfoods.add(new Food("酸菜牛肉汤", 38, R.drawable.suncainiuroumian, false, false, true));
        mfoods.add(new Food("西芹百合", 16, R.drawable.xiqin, false, false, false));
        mfoods.add(new Food("酸辣汤", 15, R.drawable.suanlatang, true, false, true));

        person=new Person();

        foodresult=new ArrayList<>();
    }

    private void findViews() {
        mNameText=findViewById(R.id.edit_name);
        mSexGroup=findViewById(R.id.sexRadioGroup);
        mHotCheckBox=findViewById(R.id.hotCheckBox);
        mFishCheckBox=findViewById(R.id.fishCheckBox);
        mSourCheckBox=findViewById(R.id.sourCheckBox);
        mSeekBar=findViewById(R.id.seekBar);
        mSeekBar.setProgress(30);
        mSearchButton=findViewById(R.id.search_btn);
        mFoodImageView=findViewById(R.id.foodImageView);
        mToggleButton=findViewById(R.id.showToggleButton);
        mToggleButton.setChecked(true);
    }

    private void setListeners() {
        //设置单选框
        mSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.man:
                        person.setSex("男");
                        break;
                    case R.id.woman:
                        person.setSex("女");
                        break;
                }
            }
        });

        //设置复选框
        mFishCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFish=isChecked;
            }
        });

        mSourCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSour=isChecked;
            }
        });


        mHotCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isHot=isChecked;

            }
        });

        //设置seekBar
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                price=seekBar.getProgress();
                Toast.makeText(MainActivity.this, "价格" + price, Toast.LENGTH_SHORT).show();
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToggleButton.isChecked()) {
                    mCurrentIndex++;
                    if (mCurrentIndex < foodresult.size()) {
                        mFoodImageView.setImageResource(foodresult.get(mCurrentIndex).getPic());
                    }
                } else {
                    if (mCurrentIndex < foodresult.size()) {
                        String foodname=foodresult.get(mCurrentIndex).getName();
                        String personName=mNameText.getText().toString();
                        String sex=person.getSex();
                        Toast.makeText(MainActivity.this, "菜名：" + foodname +
                                "\n人名：" + personName +
                                "\n性别：" + sex, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "没有啦", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void search() {
        //遍历所有菜
        //符合加入列表
        if (foodresult == null) {
            foodresult=new ArrayList<>();
        }
        //结果清空
        foodresult.clear();
        //当前显示
        mCurrentIndex=0;
        for (int i=0; i < mfoods.size(); i++) {
            Food food=mfoods.get(i);
            if (food != null) {
                //价格小于设定价格且满足顾客口味
                if (food.getPrice() < price &&
                        (food.isHot() == isHot && food.isFish() == isFish && food.isSour() == isSour)
                        ) {
                    foodresult.add(food);
                }
            }
        }
        if (mCurrentIndex < foodresult.size()) {
            mFoodImageView.setImageResource(foodresult.get(mCurrentIndex).getPic());
        }
    }


}
