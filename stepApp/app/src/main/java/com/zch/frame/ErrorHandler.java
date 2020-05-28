package com.zch.frame;

import android.content.Context;
import android.util.Log;

public class ErrorHandler implements Thread.UncaughtExceptionHandler{
    private static ErrorHandler instance;

    private ErrorHandler(){}

    public static ErrorHandler getInstance(){
        if(instance==null){
            instance = new ErrorHandler();
        }
        return instance;
    }

    public void setErrorHandler(Context ctx){
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogWriter.logToFile("崩溃简短信息:" + ex.getMessage());
        LogWriter.logToFile("崩溃简短信息:" + ex.toString());
        LogWriter.logToFile("崩溃线程名称:" + thread.getName() + "崩溃线程ID:" + thread.getId());
        final StackTraceElement[] trace = ex.getStackTrace();
        for (final StackTraceElement element : trace)
        {
            LogWriter.debugError("Line " + element.getLineNumber() + " : " + element.toString());
        }
        ex.printStackTrace();
        FrameApplication.exitApp();


    }
}
