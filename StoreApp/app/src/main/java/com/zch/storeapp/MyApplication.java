package com.zch.storeapp;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.zch.storeapp.model.DaoMaster;
import com.zch.storeapp.model.DaoSession;

public class MyApplication extends Application {

    public static DaoSession mySession;

    @Override
    public void onCreate() {
        super.onCreate();

        initDb();
    }


    /**
     * 连接数据库创建会话
     */
    public void initDb(){
        //1.获取需要连接的数据库
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this,"store.db");
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();

        //2.创建数据库连接
        DaoMaster master = new DaoMaster(db);

        //3.创建数据库会话
        mySession=master.newSession();

    }
}
