package com.zchhh.servicesolution;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button click;
    private TextView text;
    private int CHANGE_TEXT = 1;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if(msg.what==CHANGE_TEXT){
                text.setText("GOODBYE!");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        click = (Button)findViewById(R.id.click);
        text = (TextView)findViewById(R.id.text);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = CHANGE_TEXT;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });

    }
}
