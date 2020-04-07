package com.zch.datastoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InternalActivity extends AppCompatActivity {
    EditText edt;
    TextView txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal);

        edt = findViewById(R.id.et_in_input);
        txt = findViewById(R.id.tv_in_show);

    }

    public void operatein(View v){
        File f = new File(getFilesDir(),"getFilesDir.txt");
        switch (v.getId()){
            case R.id.btn_in_save:
                try{
                    if(!f.exists()){
                        f.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(edt.getText().toString().getBytes());
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }

                break;

            case R.id.btn_in_read:
                try {
                    FileInputStream fis = new FileInputStream(f);
                    byte[] b = new byte[1024];
                    int len = fis.read(b);
                    String str2 = new String(b,0,len);
                    txt.setText(str2);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

        }
    }
}
