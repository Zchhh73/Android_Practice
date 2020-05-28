package com.zch.stepapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zch.frame.LogWriter;
import com.zch.service.PedometerSerivce;
import com.zch.utiles.Settings;
import com.zch.utiles.Utiles;

public class SettingActivity extends BaseActivity {

    private ListView setting_listview;
    private ImageView back_image;
    private SettingListAdapter mAdapter;

    private IPedometerService remoteSerivce;



    @Override
    protected void onInitVariable() {

    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        setting_listview = findViewById(R.id.id_lv_listview);
        back_image = findViewById(R.id.id_iv_back);
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter = new SettingListAdapter();
        setting_listview.setAdapter(mAdapter);
    }

    private ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteSerivce=IPedometerService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onRequestData() {
        Intent serviceIntent=new Intent(this, PedometerSerivce.class);
        if (!Utiles.isServiceRunning(this, PedometerSerivce.class.getName())) {
            //启动服务
            startService(serviceIntent);
        } else {
            //服务已启动
            serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        boolean bindService = bindService(serviceIntent,mServiceConnection,BIND_AUTO_CREATE);
    }

    public class SettingListAdapter extends BaseAdapter{

        private Settings mSettings = null;
        private String[] listTitle = {"设置步长","设置体重","传感器灵敏度","传感器采样时间"};

        public SettingListAdapter(){
            mSettings = new Settings(SettingActivity.this);
        }

        @Override
        public int getCount() {
            return listTitle.length;
        }

        @Override
        public Object getItem(int position) {
            if(listTitle!=null && position<listTitle.length){
                return listTitle[position];
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView==null){
                viewHolder=new ViewHolder();
                convertView = View.inflate(SettingActivity.this,R.layout.item_setting,null);
                viewHolder.title = (TextView)convertView.findViewById(R.id.title);
                viewHolder.desc = (TextView)convertView.findViewById(R.id.desc);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(listTitle[position]);
            switch (position){
                case 0:
                    final float stepLen = mSettings.getStepLength();
                    viewHolder.desc.setText(String.format("计算距离和消耗的热量：%s CM",stepLen));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            stepClick(stepLen);
                        }
                    });
                    break;
                case 1:
                    final float bodyWeight = mSettings.getBodyWeight();
                    viewHolder.desc.setText(String.format("通过体重计算消耗的热量：%s Kg",bodyWeight));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            weightClick(bodyWeight);
                        }
                    });
                    break;
                case 2:
                    double sensitivity = mSettings.getSensitivity();
                    viewHolder.desc.setText(String.format("传感器的敏感程度的：%s",Utiles.getFormatVal(sensitivity,"#.00")));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sensitivityClick();
                        }
                    });
                    break;
                case 3:
                    int interval = mSettings.getInterval();
                    viewHolder.desc.setText(String.format("每隔：%s毫秒进行一次数据采集", Utiles.getFormatVal(interval,"#.00")));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TimeClick();
                        }
                    });
                    break;
                default:
                    LogWriter.d("Position="+position);
                    break;

            }

            return convertView;
        }

        /**
         * 设置步长
         * @param stepLen
         */
        private void stepClick(float stepLen) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            builder.setTitle("设置步长");
            View view = View.inflate(SettingActivity.this,R.layout.view_dlg_input,null);
            final EditText input = (EditText) view.findViewById(R.id.input);
            input.setText(String.valueOf(stepLen));
            builder.setView(view);
            builder.setNegativeButton("取消",null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String val = input.getText().toString();
                    if(val != null && val.length()>0){
                        float len = Float.parseFloat(val);
                        mSettings.setStepLength(len);
                        if(mAdapter!=null){
                            mAdapter.notifyDataSetChanged();
                        }
                    }else{
                        Toast.makeText(SettingActivity.this,"请输入正确参数",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.create().show();
        }

        /**
         * 设置体重
         * @param weight
         */
        private void weightClick(float weight) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            builder.setTitle("设置体重");
            View view = View.inflate(SettingActivity.this,R.layout.view_dlg_input,null);
            final EditText input = (EditText) view.findViewById(R.id.input);
            input.setText(String.valueOf(weight));
            builder.setView(view);
            builder.setNegativeButton("取消",null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String val = input.getText().toString();
                    if(val != null && val.length()>0){
                        float bodyWeight = Float.parseFloat(val);
                        mSettings.setBodyWeight(bodyWeight);
                        if(mAdapter!=null){
                            mAdapter.notifyDataSetChanged();
                        }
                    }else{
                        Toast.makeText(SettingActivity.this,"请输入正确参数",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.create().show();
        }

        private void sensitivityClick(){
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            builder.setItems(R.array.sensitive_array, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //调用服务，设置灵敏度
                    if(remoteSerivce!=null){
                        try {
                            remoteSerivce.setSensitivity(Settings.SENSITIVE_ARRAY[which]);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    mSettings.setSensitivity(Settings.SENSITIVE_ARRAY[which]);
                    if(mAdapter!=null){
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
            builder.setTitle("传感器灵敏度");
            builder.create().show();
        }

        private void TimeClick() {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            builder.setItems(R.array.interval_array, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //调用服务，设置时间间隔
                    if(remoteSerivce!=null){
                        try {
                            remoteSerivce.setInterval(Settings.INTERVAL_ARRAY[which]);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    mSettings.setInterval(Settings.INTERVAL_ARRAY[which]);
                    if(mAdapter!=null){
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
            builder.setTitle("设置传感器采样时间");
            builder.create().show();

        }


    }
    static class ViewHolder{
        TextView title;
        TextView desc;
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();

    }
}
