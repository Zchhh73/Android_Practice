package com.zch.frame;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;

public class FrameApplication extends Application {

    private static LinkedList<Activity> actList=new LinkedList<>();
    private PrefsManager mPrefsManager;
    private static FrameApplication Instance;
    private ErrorHandler mErrorHandler;

    public static FrameApplication getInstance(){
        return Instance;
    }

    public LinkedList<Activity> getActList() {
        return actList;
    }

    public PrefsManager getPrefsManager() {
        return mPrefsManager;
    }

    /**
     * 添加到列表
     *
     * @param activity
     */
    public static void addToActivityList(final Activity activity) {
        if (activity != null) {
            actList.add(activity);
        }
    }

    public static void removeFromActivityList(final Activity activity) {
        if (actList != null && actList.size() > 0 && actList.indexOf(activity) != -1) {
            actList.remove(activity);
        }
    }

    public static void clearActivityList(){
        for(int i = actList.size()-1;i>=0;i--){
            final Activity act = actList.get(i);
            if(act!=null){
                act.finish();
            }
        }
    }

    public static void exitApp(){
        try{
            clearActivityList();
        }catch (Exception e){

        }finally{
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        mPrefsManager = new PrefsManager(this);
        mErrorHandler = ErrorHandler.getInstance();
    }
}
