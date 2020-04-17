package com.zch.contentproviderdemo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {
    private static final String TAG="MyContentProvider";
    SQLiteDatabase db;
    UriMatcher matcher;

    //URI的解析
    //1.UriMatcher：指定匹配规则，匹配传入的Uri。
    //2.Uri自带解析方法。

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int result = 0;
        int code = matcher.match(uri);
        switch (code){
            case 1000:
                Log.d(TAG, "匹配路径为helloworld.");
                break;
            default:
                Log.d(TAG, "删除语句.");
                result = db.delete("info_tb",selection,selectionArgs);
                break;

        }
        return result;

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        Log.d(TAG, "调用DataProviderDemo的方法");
        long id = 0;
        if(values.size()>0){
            id = db.insert("info_tb",null,values);
        }else{
            //Uri解析
            String authority = uri.getAuthority();
            String path = uri.getPath();
            String query = uri.getQuery();
            String name = uri.getQueryParameter("name");
            String age = uri.getQueryParameter("age");
            String gender = uri.getQueryParameter("gender");
            Log.d(TAG, "主机名："+authority+"，路径："+path+",查询数据："+query
                    +"\n 姓名："+name+",年龄："+age+"，性别"+gender);
            values.put("name",name);
            values.put("age",age);
            values.put("gender",gender);
            id = db.insert("info_tb",null,values);



        }
        //用于将id追加到uri后面
        return ContentUris.withAppendedId(uri,id);

    }

    //ContentProvider创建调用
    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        SQLiteOpenHelper helper = new SQLiteOpenHelper(getContext(),"stu.db",null,1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                String sql = "create table info_tb(_id integer primary key autoincrement," +
                        "name varchar(20)," +
                        "age integer," +
                        "gender varchar(2))";
                db.execSQL(sql);

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };

        db = helper.getReadableDatabase();

        //无法匹配
        // name，
        // 路径:content://com.zch.myprovider/helloworld
        // 匹配码 返回值
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI("com.zch.myprovider","helloworld",1000);
        matcher.addURI("com.zch.myprovider","helloworld/abc",1001);
        //匹配所有helloworld下内容
        matcher.addURI("com.zch.myprovider","helloworld/*",1002);
        //匹配所有helloworld下数字内容
        matcher.addURI("com.zch.myprovider","helloworld/#",1003);

        //
        //        matcher.match()
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        //参数2：查询的列
        //参数3：查询条件
        //参数4：条件值

        Cursor cursor = db.query("info_tb",projection,selection,selectionArgs,null,null,sortOrder);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int result = db.update("info_tb",values,selection,selectionArgs);
        return result;
    }
}
