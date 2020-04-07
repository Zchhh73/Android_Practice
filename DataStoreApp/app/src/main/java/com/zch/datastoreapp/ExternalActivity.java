package com.zch.datastoreapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExternalActivity extends AppCompatActivity {
    private EditText infoedt;
    private TextView txt;
    private Button btn_read,btn_write;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external);

        infoedt=findViewById(R.id.et_input);
        txt=findViewById(R.id.tv_show);
        btn_read = findViewById(R.id.btn_read);
        btn_write = findViewById(R.id.btn_save);

        //请求权限
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){
            //动态申请权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }



    }

    public void operate(View v) {
        String path=Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/zchhh.txt";
        Log.e("TAG", path);
//        if(Environment.getExternalStorageState().equals("mounted"))
        switch (v.getId()) {
            case R.id.btn_read:
                try {
                    FileInputStream fis = new FileInputStream(path);
                    byte[] b = new byte[1024];
                    int len = fis.read(b);
                    String str2 = new String(b,0,len);
                    txt.setText(str2);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }

                break;

            case R.id.btn_save:
                File f=new File(path);
                try {
                    if (!f.exists()) {
                        f.createNewFile();
                    }
                    //true表示追加
                    FileOutputStream fos=new FileOutputStream(path, true);
                    String str=infoedt.getText().toString();
                    fos.write(str.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
