package com.zch.butterapp;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.id_tv)
    TextView mTextView;

    @BindView(R.id.id_btn1)
    Button mButton1;

    @BindView(R.id.id_btn2)
    Button mButton2;

    @BindString(R.string.helloworld)
    String str;

   


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mTextView.setText(str);
        mButton1.setText("Hello");
        mButton2.setText("Zchhh");
    }

    @OnClick({R.id.id_btn1,R.id.id_btn2})
    public void btnClick(View view){
        switch (view.getId()){
            case R.id.id_btn1:
                Toast.makeText(this, "btn1 clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_btn2:
                Toast.makeText(this, "btn2 clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
