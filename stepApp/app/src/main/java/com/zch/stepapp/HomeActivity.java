package com.zch.stepapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.zch.beans.PedometerChartBean;
import com.zch.frame.LogWriter;
import com.zch.service.PedometerSerivce;
import com.zch.utiles.Utiles;
import com.zch.widgets.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private CircleProgressBar mProgressBar;
    private TextView textCalorie;
    private TextView time;
    private TextView distance;
    private TextView stepCount;
    private Button reset;
    private Button start;
    private BarChart dataChart;
    private ImageView setting;
    //服务
    private IPedometerService remoteSerivce;
    private int status=-1;
    private static final int STATUS_NOT_RUNNING=0;
    private static final int STATUS_RUNNING=1;

    private boolean isRunning=false;
    private boolean isChartUpdate=false;

    //handler消息
    private static final int MESSAGE_UPDATE_STEP_COUNT=1000;
    private static final int MESSAGE_UPDATE_CHART_DATA=2000;
    private static final int GET_DATA_TIME=200;
    private static final long GET_CHART_DATA_TIME=60000L;

    private PedometerChartBean mChartBean;

    private boolean bindService=false;


    @Override
    protected void onInitVariable() {

    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        mProgressBar=findViewById(R.id.progressBar);
        mProgressBar.setProgress(5000);
        mProgressBar.setMaxProgress(10000);
        textCalorie=findViewById(R.id.id_tv_textCalorie);
        time=findViewById(R.id.id_tv_time);
        distance=findViewById(R.id.id_tv_distance);
        stepCount=findViewById(R.id.id_tv_stepCount);
        reset=findViewById(R.id.id_btn_reset);
        start=findViewById(R.id.id_btn_start);
        dataChart=findViewById(R.id.chart1);
        setting=findViewById(R.id.id_iv_setting);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("确认重置");
                builder.setMessage("您的记录将要被清除，确定？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(remoteSerivce!=null){
                            try {
                                remoteSerivce.stepCount();
                                remoteSerivce.resetCount();
                                mChartBean = remoteSerivce.getChartData();
                                updateChart(mChartBean);
                                status = remoteSerivce.getServiceStatus();
                                if(status==PedometerSerivce.STATUS_RUNNING){
                                    start.setText("停止");
                                }else if(status == PedometerSerivce.STATUS_NOT_RUN){
                                    start.setText("启动");
                                }
                            } catch (RemoteException e) {
                                LogWriter.d(e.toString());
                            }
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消",null);
                AlertDialog resetDlg = builder.create();
                resetDlg.show();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    status=remoteSerivce.getServiceStatus();
                } catch (RemoteException e) {
                    LogWriter.d(e.toString());
                }
                if (status == STATUS_RUNNING && remoteSerivce != null) {
                    try {
                        remoteSerivce.stepCount();
                        start.setText("启动");
                        isRunning=false;
                        isChartUpdate=false;
                    } catch (RemoteException e) {
                        LogWriter.d(e.toString());
                    }
                } else if (status == STATUS_NOT_RUNNING && remoteSerivce != null) {
                    try {
                        remoteSerivce.startCount();
                        startStepCount();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteSerivce=IPedometerService.Stub.asInterface(service);
            try {
                status=remoteSerivce.getServiceStatus();
                if (status == STATUS_RUNNING) {
                    startStepCount();
                } else {
                    start.setText("启动");
                }
            } catch (RemoteException e) {

                LogWriter.d(e.toString());
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteSerivce=null;
        }
    };


    @Override
    protected void onRequestData() {
        //检查服务是否运行
        //没运行，启动服务；若已启动，直接绑定服务
        Intent serviceIntent=new Intent(this, PedometerSerivce.class);
        if (!Utiles.isServiceRunning(this, PedometerSerivce.class.getName())) {
            //启动服务
            startService(serviceIntent);
        } else {
            //服务已启动
            serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        //绑定服务
        bindService=bindService(serviceIntent, mServiceConnection, BIND_AUTO_CREATE);
        //初始化状态
        if (bindService && remoteSerivce != null) {
            try {
                status = remoteSerivce.getServiceStatus();
                if(status==PedometerSerivce.STATUS_NOT_RUN){
                    start.setText("启动");
                }else if(status==PedometerSerivce.STATUS_RUNNING){
                    start.setText("停止");
                    isRunning=true;
                    isChartUpdate=true;
                    //启动两个线程，定时获取数据，刷新UI
                    new Thread(new StepRunnable()).start();
                    new Thread(new ChartRunnable()).start();

                }
            } catch (RemoteException e) {
                LogWriter.d(e.toString());
            }
        }else{
            start.setText("启动");
        }
    }

    private void startStepCount() throws RemoteException {
        start.setText("停止");
        isChartUpdate=true;
        isRunning=true;
        mChartBean=remoteSerivce.getChartData();
        updateChart(mChartBean);
        //启动两个线程，定时获取数据，刷新UI
        new Thread(new StepRunnable()).start();
        new Thread(new ChartRunnable()).start();
    }

    /**
     * 更新步数线程
     */
    private class StepRunnable implements Runnable {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    status=remoteSerivce.getServiceStatus();
                    if (status == STATUS_RUNNING) {
                        mHandler.removeMessages(MESSAGE_UPDATE_STEP_COUNT);
                        //更新数据
                        mHandler.sendEmptyMessage(MESSAGE_UPDATE_STEP_COUNT);
                        Thread.sleep(GET_DATA_TIME);
                    }
                } catch (RemoteException e) {
                    LogWriter.d(e.toString());
                } catch (InterruptedException e) {
                    LogWriter.d(e.toString());
                }
            }
        }
    }

    /**
     * 更新图表线程
     */
    private class ChartRunnable implements Runnable {
        @Override
        public void run() {
            while (isChartUpdate) {
                try {
                    mChartBean=remoteSerivce.getChartData();
                    mHandler.removeMessages(MESSAGE_UPDATE_CHART_DATA);
                    mHandler.sendEmptyMessage(MESSAGE_UPDATE_CHART_DATA);
                    Thread.sleep(GET_CHART_DATA_TIME);

                } catch (RemoteException e) {
                    LogWriter.d(e.toString());
                } catch (InterruptedException e) {
                    LogWriter.d(e.toString());
                }
            }
        }
    }


    Handler mHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_UPDATE_STEP_COUNT:
                    //更新计步器
                    updateStepCount();
                    break;
                case MESSAGE_UPDATE_CHART_DATA:
                    if (mChartBean != null) {
                        updateChart(mChartBean);
                    }
                    break;
                default:
                    LogWriter.d("Default= " + msg.what);

            }
            super.handleMessage(msg);
        }
    };

    private void updateChart(PedometerChartBean chartBean) {
        List<String> xVals=new ArrayList<String>();
        List<BarEntry> yVals=new ArrayList<BarEntry>();
        if (chartBean != null) {
            for (int i=0; i <= chartBean.getIndex(); i++) {
                xVals.add(String.valueOf(i) + "分s");
                int valY=chartBean.getArrayData()[i];
                yVals.add(new BarEntry(valY, i));
            }
            time.setText(String.valueOf(chartBean.getIndex()) + "分");
            BarDataSet set1=new BarDataSet(yVals, "所走的步数");
            set1.setBarBorderWidth(2f);
//            List<BarDataSet> dataSets =new ArrayList<BarDataSet>();
//            dataSets.add(set1);
            BarData data=new BarData(set1);
            data.setValueTextSize(10f);
            dataChart.setData(data);
            dataChart.invalidate();
        }

    }

    /**
     * 更新计步数据
     */
    public void updateStepCount() {
        if (remoteSerivce != null) {
            int stepCountVal=0;
            double calorieVal=0;
            double distanceVal=0;
            try {
                stepCountVal=remoteSerivce.getStepsCount();
                calorieVal=remoteSerivce.getCalorie();
                distanceVal=remoteSerivce.getDistance();
            } catch (RemoteException e) {
                LogWriter.d(e.toString());
            }
            stepCount.setText(String.valueOf(stepCountVal) + "步");
            textCalorie.setText(Utiles.getFormatVal(calorieVal) + "卡");
            distance.setText(Utiles.getFormatVal(distanceVal));
            mProgressBar.setProgress(stepCountVal);


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bindService){
            bindService=false;
            isRunning=false;
            isChartUpdate=false;
            unbindService(mServiceConnection);
        }
    }
}
