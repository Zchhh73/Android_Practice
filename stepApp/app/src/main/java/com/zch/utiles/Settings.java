package com.zch.utiles;

import android.content.Context;

import com.zch.frame.PrefsManager;

/**
 * 保存配置
 */
public class Settings {
    public static final float[] SENSITIVE_ARRAY={1.97f, 2.96f, 4.44f, 6.66f, 10.0f, 15.0f, 22.50f, 33.75f, 50.62f};
    public static final int[] INTERVAL_ARRAY={100, 200, 300, 400, 500, 600, 700, 800};
    public static final String SENSITIVITY = "sensitivity";
    public static final String INTERVAL = "interval";
    public static final String STEP_LEN = "steplen";
    public static final String BODY_WEIGHT = "bodyweight";
    private PrefsManager mPrefsManager;

    public Settings(Context context){
        mPrefsManager = new PrefsManager(context);
    }

    /**
     * 获得传感器灵敏度
     * @return
     */
    public double getSensitivity(){
        float sensitivity = mPrefsManager.getFloat(SENSITIVITY);
        if(sensitivity==0.0f){
            return 10.0f;
        }
        return sensitivity;
    }

    public void setSensitivity(float sensitivity){
        mPrefsManager.putFloat(SENSITIVITY,sensitivity);
    }

    public int getInterval(){
        int interval = mPrefsManager.getInt(INTERVAL);
        if(interval==0){
            return 200;
        }
        return interval;
    }

    public void setInterval(int interval){
        mPrefsManager.putInt(INTERVAL,interval);
    }

    public float getStepLength(){
        float stepLength = mPrefsManager.getFloat(STEP_LEN);
        if(stepLength==0.0f){
            return 50.0f;
        }
        return stepLength;
    }

    public void setStepLength(float stepLength){
        mPrefsManager.putFloat(STEP_LEN,stepLength);
    }

    public float getBodyWeight(){
        int interval = mPrefsManager.getInt(INTERVAL);
        if(interval==0){
            return 60.0f;
        }
        return interval;
    }

    public void setBodyWeight(float weight){
        mPrefsManager.putFloat(BODY_WEIGHT,weight);
    }


}
