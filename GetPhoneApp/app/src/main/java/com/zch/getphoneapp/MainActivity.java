package com.zch.getphoneapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    ContentResolver resolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //请求权限
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
        if(permission != PackageManager.PERMISSION_GRANTED){
            //动态申请权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS},1);
        }

        findViewById(R.id.sms_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.获取内容处理者
                 resolver = getContentResolver();

                //2.查询方法
                //short message service

                /*短信箱：发件箱、收件箱、草稿箱
                * content://sms 短信箱（所有）
                * content://sms/inbox 收件箱
                * content://sms/sent  发件箱
                * content://sms/draft  草稿箱
                * */
                Uri uri = Uri.parse("content://sms");
                Cursor cursor=resolver.query(uri, null, null, null, null);
                //3.解析Cursor
                while(cursor.moveToNext()){
                    //对象，内容
                    String msg = "";

                    String address = cursor.getString(cursor.getColumnIndex("address"));
                    String body = cursor.getString(cursor.getColumnIndex("body"));

                    msg = address + ":" +body;
                    Log.d("TAG", msg);
                    //遍历列
                    /*
                    for(int i = 0;i< cursor.getColumnCount();i++){
                        Log.d("TAG", cursor.getString(i)+" ");
                    }
                                         */
                }
            }

        });

        findViewById(R.id.read_contact_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolver = getContentResolver();

                //对于联系人，姓名所在的表是主表，其他内容位于从表
                //主表中的主键会在从表中作为外键使用
                Cursor query=resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                while(query.moveToNext()){
                    String meg="";
//                    ContactsContract.Contacts.DISPLAY_NAME 姓名
//                    ContactsContract.Contacts._ID 主键
                    String name = query.getString(query.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String _id = query.getString(query.getColumnIndex(ContactsContract.Contacts._ID));



                    String selections = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
                    Cursor c = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            selections,
                            new String[]{_id},
                            null);
                    while(c.moveToNext()){
                        String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        meg = name + ":" + number;
                    }
                    Log.d("TAG", meg);
                }

            }
        });

        findViewById(R.id.add_contact_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolver = getContentResolver();

                //1.往ContentProvider插入用一条空数据，获取id
                //2.利用id组合姓名和电话号码，向另一个ContentProvider中插入

                //添加空记录
                ContentValues values = new ContentValues();
                Uri uri = resolver.insert(ContactsContract.RawContacts.CONTENT_URI,values);
                long id=ContentUris.parseId(uri);

                //添加姓名
                values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,"ZCHHH");
                //姓名与id绑定
                values.put(ContactsContract.Data.RAW_CONTACT_ID,id);
                //指定该行数据类型
                values.put(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                resolver.insert(ContactsContract.Data.CONTENT_URI,values);


                //添加电话
                values.clear();
                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,"12343215678");
                values.put(ContactsContract.Data.RAW_CONTACT_ID,id);
                //指定该行数据类型
                values.put(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                //指定联系方式类型
                values.put(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);

                resolver.insert(ContactsContract.Data.CONTENT_URI,values);
            }
        });
    }
}
