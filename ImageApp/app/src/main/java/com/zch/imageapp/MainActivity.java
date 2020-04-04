package com.zch.imageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String PATH="https://img2.mukewang.com/5adfee7f0001cbb906000338-240-135.jpg";

    private Button img_btn;
    private ImageView img;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 100){
                Bitmap bm = (Bitmap) msg.obj;
                img.setImageBitmap(bm);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        img_btn=findViewById(R.id.image_btn);
        img=findViewById(R.id.image);
        img_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_btn:
                GetPicThread PicThread=new GetPicThread(PATH, handler);
                Thread thread=new Thread(PicThread);
                thread.start();
                break;
        }
    }
}
