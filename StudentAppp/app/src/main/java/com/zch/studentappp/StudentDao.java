package com.zch.studentappp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class StudentDao {

    private SQLiteDatabase db;

    public StudentDao(final Context context) {
        String path=Environment.getExternalStorageDirectory() + "/stu.db";
        SQLiteOpenHelper helper=new SQLiteOpenHelper(context, path, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            }
        };

        db=helper.getReadableDatabase();
    }

    public void addStudent(Student stu) {
        String sql="insert into info_tb(name,age,gender) values(?,?,?)";
        db.execSQL(sql, new String[]{stu.getName(), stu.getAge() + "", stu.getGender()});
    }


    public Cursor getStudent(String... strs) {
        //查询所有(没有参数)
        String sql="select * from info_tb";
        //条件查询（参数形式：第一个参数条件，第二个条件值）
        if (strs.length != 0) {
            sql+=" where " + strs[0] + "='" + strs[1] + "'";
        }
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }

    public void deleteStudent(String... strs) {
        String sql = "delete from info_tb where "+strs[0]+"='"+strs[1]+"'";
        db.execSQL(sql);


    }

    public void updateStudent(Student stu) {
        String sql = "update info_tb set name =?,age=?, gender=? where _id=?";
        db.execSQL(sql,new Object[]{stu.getName(),stu.getAge(),stu.getGender(),stu.getId()});

    }
}
