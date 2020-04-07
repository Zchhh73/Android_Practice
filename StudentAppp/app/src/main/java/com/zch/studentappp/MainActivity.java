package com.zch.studentappp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText nameEdt, ageEdt, idEdt;
    private RadioGroup genderGp;
    private RadioButton btn_male;
    private String genderStr="男";
    private SQLiteDatabase db;
    private ListView stuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SQLiteOpenHelper
        String path=Environment.getExternalStorageDirectory() + "/stu.db";
        Log.e("TAG", path);
        SQLiteOpenHelper helper=new SQLiteOpenHelper(this, path, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                Toast.makeText(MainActivity.this, "数据库创建", Toast.LENGTH_SHORT).show();
                //数据库不存在，调用onCreate方法.
                        /*
                        String sql = "create table test_tb(_id integer primary key autoincrement," +
                                "name varchar(20) not null," +
                                "age integer)";
                        db.execSQL(sql);

                       */
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                //升级
                Toast.makeText(MainActivity.this, "数据库升级", Toast.LENGTH_SHORT).show();
            }
        };

        //用于获取数据库对象
        db=helper.getReadableDatabase();
//                db.rawQuery()
//                db.execSQL();


        nameEdt=findViewById(R.id.name_edt);
        ageEdt=findViewById(R.id.age_edt);
        idEdt=findViewById(R.id.id_edt);
        stuList=findViewById(R.id.stu_list);
        btn_male = findViewById(R.id.male);

        genderGp=findViewById(R.id.gender_group);
        genderGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male) {
                    //男
                    genderStr="男";
                } else {
                    //女
                    genderStr="女";
                }
            }
        });

        int permission=ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }

    }


    public void myClick(View v) {
        String nameStr=nameEdt.getText().toString();
        String ageStr=ageEdt.getText().toString();
        String idStr=idEdt.getText().toString();
        switch (v.getId()) {
            case R.id.insert_btn:
                /*
                //SQL语句操作
                String sql = "insert into info_tb (name,age,gender) values ('"+nameStr+"',"+ageStr+",'"+genderStr+"')";
                String sql="insert into info_tb(name,age,gender) values (?,?,?)";
                db.execSQL(sql, new String[]{nameStr, ageStr, genderStr});
                Toast.makeText(MainActivity.this, "数据添加成功", Toast.LENGTH_SHORT).show();
                 */

                //使用API操作
                //insert(),delete(),update(),query()
                /*
                参数1：表名；
                参数2：可以为空的列，如果第三个值为null,
                */
                ContentValues values = new ContentValues();
                values.put("name",nameStr);
                values.put("age",ageStr);
                values.put("gender",genderStr);
                long id=db.insert("info_tb", null, values);
                Toast.makeText(MainActivity.this, "数据添加成功,学号是："+id, Toast.LENGTH_SHORT).show();


                break;
            case R.id.select_btn:
                /*
                //select * from 表名 where _id = ?
                String sql2="select * from info_tb";
                if (!idStr.equals("")) {
                    sql2+=" where _id=" + idStr;
                }
                //查询结果
                Cursor cursor=db.rawQuery(sql2, null);
                 */
                /*
                * 参数2：查询的列{"name","age"..}（null）为查所有
                * 参数3：条件
                * 参数5：分组
                * 参数6：当groupby对数据分组后，通过having去除不符合条件的组。
                * */
                Cursor cursor = db.query("info_tb",null,null,null,null,null,null);
                //SimpleCursorAdapter
                SimpleCursorAdapter adapter=new SimpleCursorAdapter(
                        this,
                        R.layout.item,
                        cursor,
                        new String[]{"_id", "name", "age", "gender"},
                        new int[]{R.id.item_id, R.id.item_name, R.id.item_age, R.id.item_sex},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                stuList.setAdapter(adapter);
                break;
            case R.id.delete_btn:
                /*
                String sql3="delete from info_tb where _id = ?";
                db.execSQL(sql3, new String[]{idStr});
                 */
                int count = db.delete("info_tb","_id=?",new String[]{idStr});
                if(count>0){
                    Toast.makeText(MainActivity.this, "数据删除成功", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.update_btn:
                /*
                String sql4="update info_tb set name= ?,age=?,gender=? where _id=?";
                db.execSQL(sql4, new String[]{nameStr,ageStr,genderStr,idStr});
                */
                ContentValues values1 = new ContentValues();
                values1.put("name",nameStr);
                values1.put("age",ageStr);
                values1.put("gender",genderStr);

                int count2 = db.update("info_tb",values1,"_id=?",new String[]{idStr});
                if(count2>0){
                    Toast.makeText(MainActivity.this, "数据修改成功", Toast.LENGTH_SHORT).show();

                }
                break;

        }
        nameEdt.setText("");
        ageEdt.setText("");
        idEdt.setText("");
        btn_male.setChecked(true);
    }
}
