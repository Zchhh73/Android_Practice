package com.zchhh.servicesolution;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn1;
    private Button btn2;
    private Button IntentService_btn;
    private Button bindService_btn;
    private Button unbindService_btn;

    private MyService.MyBinder binder;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        IntentService_btn = (Button)findViewById(R.id.btn3);
        bindService_btn = (Button)findViewById(R.id.btn4);
        unbindService_btn = (Button)findViewById(R.id.btn5);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        IntentService_btn.setOnClickListener(this);
        bindService_btn.setOnClickListener(this);
        unbindService_btn.setOnClickListener(this);

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MyService.MyBinder) service;
            binder.createProgressDialog();
            binder.onProgressUpdate();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                Intent startintent = new Intent(this,MyService.class);
                startService(startintent);
                break;
            case R.id.btn2:
                Intent stopintent = new Intent(this,MyService.class);
                stopService(stopintent);
                break;
            case R.id.btn3:
                Intent intentservice = new Intent(this,MyIntentService.class);
                startService(intentservice);
                Log.i("Main", "Thread is "+Thread.currentThread().getId());
                break;
            case R.id.btn4:
                Intent bindIntent = new Intent(this,MyService.class);
                bindService(bindIntent,connection,BIND_AUTO_CREATE);
                Toast.makeText(this,"Activity与Service绑定",Toast.LENGTH_SHORT);
                break;
            case R.id.btn5:
                unbindService(connection);
                break;
        }
    }
}
