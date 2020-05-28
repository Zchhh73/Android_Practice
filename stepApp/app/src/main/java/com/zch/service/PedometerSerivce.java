package com.zch.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import com.zch.beans.PedometerChartBean;
import com.zch.db.DBHelper;
import com.zch.frame.FrameApplication;
import com.zch.utiles.ACache;
import com.zch.utiles.Settings;
import com.zch.utiles.Utiles;
import com.zch.beans.PedometerBean;
import com.zch.stepapp.IPedometerService;

import androidx.annotation.Nullable;

public class PedometerSerivce extends Service {


    private SensorManager mSensorManager;
    private PedometerBean mPedometerBean;
    private PedometerListener mPedometerListener;

    public static final int STATUS_NOT_RUN=0;
    public static final int STATUS_RUNNING=1;
    private int runStatus=STATUS_NOT_RUN;
    private static final long SAVE_CHART_TIME = 60000L;

    private Settings mSettings;
    private PedometerChartBean mPedometerChartBean;
    private static Handler handler = new Handler();


    public double getCalorieBySteps(int stepCount) {
        //步长
        float stepLen=mSettings.getStepLength();
        //体重
        float bodyWeight=mSettings.getBodyWeight();
        double METRIC_WALKING_FACTOR=0.708;//走路
        double METRIC_RUNNING_FACTOR=1.02784823;//跑步
        //跑步热量(kcal) = 体重(kg) * 距离(公里) * 1.02784823
        //走路热量(kcal) = 体重(kg) * 距离(公里) * 0.708
        double calories=(bodyWeight * METRIC_WALKING_FACTOR) * stepLen * stepCount / 100000.0;
        return calories;
    }

    public double getStepDistance(int stepCount) {
        //步长
        float stepLen=mSettings.getStepLength();
        double distance=(stepCount * (long) (stepLen)) / 100000.0f;
        return distance;
    }

    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            if(runStatus==STATUS_RUNNING){
                if(handler!=null && mPedometerChartBean != null){
                    handler.removeCallbacks(timeRunnable);
                    updateChartData();//更新数据
                    handler.postDelayed(timeRunnable,SAVE_CHART_TIME);
                }
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        mPedometerBean=new PedometerBean();
        mPedometerListener=new PedometerListener(mPedometerBean);
        mPedometerChartBean = new PedometerChartBean();
        mSettings=new Settings(this);
    }

    //更新计步器图表数据
    private void updateChartData(){
        if(mPedometerChartBean.getIndex()<1440-1){
            mPedometerChartBean.setIndex(mPedometerChartBean.getIndex()+1);
            mPedometerChartBean.getArrayData()[mPedometerChartBean.getIndex()]=
                    mPedometerBean.getStepCount();
        }
    }

    private void saveChartData(){
        String jsonStr = Utiles.objToJson(mPedometerChartBean);
        ACache.get(FrameApplication.getInstance()).put("JsonChartData",jsonStr);

    }

    private IPedometerService.Stub mIPedometerService=new IPedometerService.Stub() {
        @Override
        public void startCount() throws RemoteException {
            if (mSensorManager != null && mPedometerListener != null) {
                Sensor sensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mSensorManager.registerListener(mPedometerListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                mPedometerBean.setStartTime(System.currentTimeMillis());
                mPedometerBean.setDay(Utiles.getTimestempByDay());//记录哪天的数据
                runStatus=STATUS_RUNNING;
                handler.postDelayed(timeRunnable,SAVE_CHART_TIME);
            }
        }

        @Override
        public void stepCount() throws RemoteException {
            if (mSensorManager != null && mPedometerListener != null) {
                Sensor sensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mSensorManager.unregisterListener(mPedometerListener, sensor);
                runStatus=STATUS_NOT_RUN;
                handler.removeCallbacks(timeRunnable);
            }
        }

        @Override
        public void resetCount() throws RemoteException {
            if (mPedometerBean != null) {
                mPedometerBean.reset();
                saveData();
            }
            if(mPedometerChartBean!=null){
                mPedometerChartBean.reset();
                saveChartData();
            }
            if (mPedometerListener != null) {
                mPedometerListener.setCurrentSteps(0);
            }
        }

        @Override
        public int getStepsCount() throws RemoteException {
            if (mPedometerBean != null) {
                return mPedometerBean.getStepCount();
            }
            return 0;
        }

        @Override
        public double getCalorie() throws RemoteException {
            if (mPedometerBean != null) {
                return getCalorieBySteps(mPedometerBean.getStepCount());
            }
            return 0;
        }

        @Override
        public double getDistance() throws RemoteException {
            return getDistanceVal();
        }


        private double getDistanceVal() {
            if (mPedometerBean != null) {
                return getStepDistance(mPedometerBean.getStepCount());
            }
            return 0;
        }


        @Override
        public void saveData() throws RemoteException {
            if (mPedometerBean != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DBHelper dbHelper=new DBHelper(PedometerSerivce.this, DBHelper.DB_NAME);
                        //设置距离
                        mPedometerBean.setDistance(getDistanceVal());
                        //设置热量消耗
                        mPedometerBean.setCalorie(getCalorieBySteps(mPedometerBean.getStepCount()));
                        //计时
                        long time=mPedometerBean.getLastStepTime() - mPedometerBean.getStartTime();
                        if(time==0){
                            mPedometerBean.setPace(0);//设置pace
                            mPedometerBean.setSpeed(0);
                        }else{
                            int pace = Math.round(60*mPedometerBean.getStepCount()/time);
                            mPedometerBean.setPace(pace);
                            long speed = Math.round((mPedometerBean.getDistance()/1000)/(time/60*60));
                            mPedometerBean.setSpeed(speed);
                        }
                        dbHelper.writeToDatabase(mPedometerBean);
                    }
                }).start();
            }
        }

        @Override
        public void setSensitivity(double sensitivity) throws RemoteException {
//            if (mSettings != null) {
//                mSettings.setSensitivity((float) sensitivity);
//            }
            if(mPedometerListener!=null){
                mPedometerListener.setSensitivity((float)sensitivity);
            }
        }

        @Override
        public double getSensitivity() throws RemoteException {
            if (mSettings != null) {
                return mSettings.getSensitivity();
            }
            return 0;
        }

        @Override
        public void setInterval(int interval) throws RemoteException {
            if (mSettings != null) {
                mSettings.setInterval(interval);
            }
            if(mPedometerListener!=null){
                mPedometerListener.setLimit(interval);
            }

        }

        @Override
        public int getInterval() throws RemoteException {
            if (mSettings != null) {
                return mSettings.getInterval();
            }
            return 0;
        }

        @Override
        public long getStartTimeStamp() throws RemoteException {
            if (mPedometerBean != null) {
                return mPedometerBean.getStartTime();
            }
            return 0L;
        }

        @Override
        public int getServiceStatus() throws RemoteException {
            return runStatus;
        }

        @Override
        public PedometerChartBean getChartData() throws RemoteException {
            return mPedometerChartBean;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mIPedometerService;
    }


}
