package com.zch.menuactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button ctx_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //注册
//        registerForContextMenu(findViewById(R.id.btn1));
        //上下文操作模式：
        /*
            1.实现ActionMode CallBack
            2.在view的长按事件中启动上下文操作模式
         */
        findViewById(R.id.btn1).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActionMode(cb);
                return false;
            }
        });

        final Button pop_btn =  findViewById(R.id.btn2);
        pop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化popupMenu对象(参数2：被锚定的view)
                PopupMenu menu = new PopupMenu(MainActivity.this,pop_btn);
                //加载菜单资源
                menu.getMenuInflater().inflate(R.menu.pop,menu.getMenu());
                //为PopupMenu设置监听器
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.rename1:
                                Toast.makeText(MainActivity.this, "重命名", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.setting1:
                                Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
                                break;

                        }
                        return true;
                    }
                });
                menu.show();

            }
        });
    }

    ActionMode.Callback cb = new ActionMode.Callback() {
        //创建，启动上下文操作模式时调用。
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Log.e("TAG", "onCreateActionMode: 创建");
            getMenuInflater().inflate(R.menu.ctx,menu);
            return true;
        }

        //创建方法后调用
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            Log.e("TAG", "onCreateActionMode: 准备");
            return false;
        }
        //点击方法
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Log.e("TAG", "onCreateActionMode: 点击");
            switch (item.getItemId()){
                case R.id.rename:
                    Toast.makeText(MainActivity.this, "重命名", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.delete:
                    Toast.makeText(MainActivity.this, "删除", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }

        //结束时被调用
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            Log.e("TAG", "onCreateActionMode: 结束");
        }
    };

    /*
    //创建上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.ctx,menu);
    }


    //菜单项的操作
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.rename:
                Toast.makeText(this, "重命名", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "删除", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
    */
    //创建OptionMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //通过xml加载菜单资源
//        getMenuInflater().inflate(R.menu.option,menu);

        //通过java实现：Menu SubMenu
        //参数：(组id,菜单项id,序号
        menu.add(1,1,1,"设置");
        SubMenu sub = menu.addSubMenu(1,2,2,"更多");
        sub.add(2,3,1,"添加");
        sub.add(2,4,2,"删除");


        return true;

    }


    //获取菜单项
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.save:
//                Toast.makeText(this, "保存", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.setting:
//                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.exit:
//                finish();
//                break;
            case 1:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
