package com.zch.dataresoverdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ContentResolver resolver;
    private EditText nameEdt,ageEdt,idEdt;
    private RadioGroup sexGp;
    private ListView stuList;

    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取resolver对象
        resolver = getContentResolver();

//        resolver.query();
//        resolver.insert();
//        resolver.delete();
//        resolver.update();

        nameEdt = findViewById(R.id.name_edt);
        ageEdt = findViewById(R.id.age_edt);
        idEdt = findViewById(R.id.id_edt);
        sexGp = findViewById(R.id.gender_group);
        stuList = findViewById(R.id.stu_list);

        sexGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.male){
                    gender  ="男";
                }else{
                    gender = "女";
                }
            }
        });

    }

    public void myClick(View v){
        Uri uri = Uri.parse("content://com.zch.myprovider");
        String name = nameEdt.getText().toString();
        String age = ageEdt.getText().toString();
        String _id = idEdt.getText().toString();

        switch(v.getId()){
            case R.id.insert_btn:
                //URI: content://authorities
                //
                ContentValues values = new ContentValues();
                values.put("name",name);
                values.put("age",age);
                values.put("gender",gender);
                Uri uri2 = resolver.insert(uri,values);
                long id = ContentUris.parseId(uri2);
                Toast.makeText(this, "新学生的学号是："+id, Toast.LENGTH_SHORT).show();
                break;
            case R.id.select_btn:
                //参数2： null 查所有
                Cursor cursor = resolver.query(uri,null,null,null,null);

                //最后一个参数，数据刷新
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                        R.layout.item,
                        cursor,
                        new String[]{"_id","name","age","gender"},
                        new int[]{R.id.item_id,R.id.item_name,R.id.item_age,R.id.item_sex},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

                stuList.setAdapter(adapter);
                break;
            case R.id.delete_btn:
                int result = resolver.delete(uri,"_id=?",new String[]{_id});
                if(result > 0){
                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.update_btn:
                ContentValues values1 = new ContentValues();
                values1.put("name",name);
                values1.put("age",age);
                values1.put("gender",gender);

                int result1 = resolver.update(uri,values1,"_id=?",new String[]{_id});
                if(result1 > 0){
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.matcher_btn:
                resolver.delete(Uri.parse("content://com.zch.myprovider/helloworld"),null,null);
                break;
            case R.id.uri_btn:
                resolver.insert(Uri.parse("content://com.zch.myprovider/whatever?name=llz&age=25&gender=男"),
                        new ContentValues());

                break;


        }

    }
}
