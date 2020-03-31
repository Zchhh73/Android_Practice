package com.zchhh.activitylife;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


          //静态加载
//        bt = (Button) findViewById(R.id.btn1);
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "您点击了按钮", Toast.LENGTH_SHORT).show();
//            }
//        });

        //动态加载
        /*
          1.先用getSupportFragmentManager()方法获取一个FragmentManager对象，
            再通过它的beginTransaction()获取一个FragmentTransaction的实例。
          2.用beginTransaction.add()方法将MyFragemnt实例添加到main布局里LinearLayout里
            第一个参数是容器视图资源Id，作用是：(1).告知FragmentManager碎片视图应出现在活动视图的地方。
                                                (2).它也是FragmentManager队列中碎片的唯一标识符
          3.调用beginTransaction.commit()提交。
         */
        //1.
        MyFragment fragment = new MyFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        //2.
        beginTransaction.add(R.id.layout,fragment);//把该碎片放到布局的layout容器中
        beginTransaction.addToBackStack(null);//允许用户通过按键返回前一个Fragment状态
        //3.
        beginTransaction.commit();

    }
}
