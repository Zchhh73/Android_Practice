package com.zchhh.mycontentprovider;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    final private int READ_CODE_ASK_PERMISSIONS = 123;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        //读取联系人电话。
        init();

        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if(hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},READ_CODE_ASK_PERMISSIONS);
            }
            return;
        }
    }

    private void init() {
        //Uri已经封装好
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //用Cursor对象进行遍历
        if(cursor!=null){
            while(cursor.moveToNext()){
                //取出联系人姓名
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                //用ID唯一表示某联系人，因为一个联系人可能有多个电话，
                int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                //取电话
                Cursor cursor1 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+id,null,null);
                if(cursor1!=null){
                    while(cursor1.moveToNext()){
                        String phone = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        list.add(name+":"+phone);
                    }
                }
                cursor1.close();
            }
            cursor.close();
        }
    }
}
