package com.zch.dialogapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void myClick(View view){
        switch (view.getId()){
            //普通对话框
            case R.id.btn1:
                //构造方法
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("退出程序么？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                builder.setNegativeButton("取消",null);
                builder.show();

                break;
            case R.id.btn2:
                MyDialog md = new MyDialog(this,R.style.myDialog);
                md.show();
                break;
            case R.id.btn3:
                showPopupWindow(view);
                break;
            case R.id.btn4:
                showArrayDialog();
                break;

        }
    }

    private void showArrayDialog() {
        final String[] items = {"Java","MySQL","Android","HTML","C++","Js"};
        //环境，布局资源索引,数据源
//        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,items);
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.item_layouot,R.id.item_txt,items);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("请选择")
                //适配器对象，监听器
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, items[which], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
        builder.show();

    }

    //设置popupwindow
    public void showPopupWindow(View view){
        //布局作为视图对象
        View v =LayoutInflater.from(this).inflate(R.layout.popup_layout,null);
        //实例化对象
        final PopupWindow window = new PopupWindow(v,490,80,true);

        //设置(背景)
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置(响应事件)
        window.setOutsideTouchable(true);
        window.setTouchable(true);

        //动画:1.创建动画资源 2.创建style应用动画资源 3.对当前弹窗动画风格设置动画资源
        window.setAnimationStyle(R.style.translate_anim);

        //展示
        //参数2,3：相对于锚在x,y方向上的偏移量
        window.showAsDropDown(view,-490,0);

        //点击事件
        v.findViewById(R.id.choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "选择", Toast.LENGTH_SHORT).show();
                window.dismiss();
            }
        });

        v.findViewById(R.id.choose_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "全选", Toast.LENGTH_SHORT).show();
                window.dismiss();
            }
        });

        v.findViewById(R.id.copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "复制", Toast.LENGTH_SHORT).show();
                window.dismiss();
            }
        });
    }
}
