package com.zch.studentappp;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity2 extends AppCompatActivity {
    private EditText nameEdt, ageEdt, idEdt;
    private RadioGroup genderGp;
    private RadioButton btn_male;
    private String genderStr="男";
    private ListView stuList;
    private StudentDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao=new StudentDao(this);

        nameEdt=findViewById(R.id.name_edt);
        ageEdt=findViewById(R.id.age_edt);
        idEdt=findViewById(R.id.id_edt);
        stuList=findViewById(R.id.stu_list);
        btn_male=findViewById(R.id.male);

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
                if(!nameStr.equals("")||ageStr.equals("")){
                    Student student=new Student();
                    student.setName(nameStr);
                    student.setAge(Integer.parseInt(ageStr));
                    student.setGender(genderStr);
                    dao.addStudent(student);
                    Toast.makeText(MainActivity2.this, "数据添加成功", Toast.LENGTH_SHORT).show();
                }else{
                    if(nameStr.equals("")){
                        Toast.makeText(MainActivity2.this, "请输入姓名", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(MainActivity2.this, "数据添加失败", Toast.LENGTH_SHORT).show();
                    }

                }
                break;

            case R.id.select_btn:

                String[] params = getParams(nameStr,ageStr,idStr);
                Cursor cursor;
                if (params[0].equals("")) {
                    cursor=dao.getStudent();
                } else {
                    cursor=dao.getStudent(params[0], params[1]);
                }

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
                String[] params1 = getParams(nameStr,ageStr,idStr);
                dao.deleteStudent(params1[0],params1[1]);
                Toast.makeText(MainActivity2.this, "数据删除成功", Toast.LENGTH_SHORT).show();
                break;


            case R.id.update_btn:
                Student stu = new Student();
                stu.setName(nameStr);
                stu.setAge(Integer.parseInt(ageStr));
                stu.setGender(genderStr);
                stu.setId(Integer.parseInt(idStr));
                dao.updateStudent(stu);
                Toast.makeText(MainActivity2.this, "数据修改成功", Toast.LENGTH_SHORT).show();
                break;

        }
        nameEdt.setText("");
        ageEdt.setText("");
        idEdt.setText("");
        btn_male.setChecked(true);
    }

    public String[] getParams(String nameStr, String ageStr, String idStr) {
        String[] params=new String[2];
        if (!nameStr.equals("")) {
            params[0]="name";
            params[1]=nameStr;
        } else if (!ageStr.equals("")) {
            params[0]="age";
            params[1]=ageStr;
        } else if (!idStr.equals("")) {
            params[0]="_id";
            params[1]=idStr;
        }else{
            params[0]="";
            params[1]="";
        }

        return params;
    }
}
