package com.zch.service;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.zch.beans.PedometerBean;

public class PedometerListener implements SensorEventListener {
    //当前步数
    private int currentSteps=0;
    //灵敏度
    private float sensitivity=30;
    //采样时间
    private long mLimit=300;
    //最后保存的值
    private float mLastValue;
    //放大值
    private float mScale=-4f;
    //偏移值
    private float offset=240f;
    //采样时间
    private long start=0;
    private long end=0;
    //方向
    private float mLastDirection;
    //记录数值
    private float mLastExtremes[][]=new float[2][1];
    //最后一次变化量
    private float mLastDiff;
    //是否匹配
    private int mLastMatch=-1;

    private PedometerBean data;

    public PedometerListener(PedometerBean data){
        this.data = data;
    }

    public void setCurrentSteps(int step){
        currentSteps = step;
    }

    public float getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(float sensitivity) {
        this.sensitivity=sensitivity;
    }

    public long getLimit() {
        return mLimit;
    }

    public void setLimit(long limit) {
        mLimit=limit;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor=event.sensor;
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float sum=0;
                //取三个方向的值
                for (int i=0; i < 3; i++) {
                    float vector=offset + event.values[i] * mScale;
                    sum+=vector;
                }
                //取得传感器平均值
                float average=sum / 3;
                float dir;
                //判断方向
                if (average > mLastValue) {
                    dir=1;
                } else if (average < mLastValue) {
                    dir=-1;
                } else {
                    dir=0;
                }
                //如果相反
                if (dir == -mLastDirection) {
                    int extType=(dir > 0 ? 0 : 1);
                    //保存变化
                    mLastExtremes[extType][0]=mLastValue;
                    //变化绝对值
                    float diff=Math.abs(mLastExtremes[extType][0] - mLastExtremes[1 - extType][0]);
                    if (diff > sensitivity) {
                        //数值是否与上次足够大
                        boolean isLargeAsPrevious=diff > (mLastDiff * 2 / 3);
                        boolean isPreviousLargeEnough=mLastDiff > (diff / 3);
                        //方向
                        boolean isNotContra=(mLastMatch != 1 - extType);
                        if (isLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                            //有效记录
                            end = System.currentTimeMillis();
                            if(end - start >mLimit){
                                currentSteps++;
                                mLastMatch = extType;
                                start = end;
                                mLastDiff = diff;
                                if(data != null){
                                    data.setStepCount(currentSteps);
                                    data.setLastStepTime(System.currentTimeMillis());
                                }
                            }else{
                                mLastDiff = sensitivity;
                            }
                        }else{
                            //未匹配
                            mLastMatch = -1;
                            mLastDiff = sensitivity;
                        }
                    }
                }
                mLastDirection = dir;
                mLastValue = average;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
