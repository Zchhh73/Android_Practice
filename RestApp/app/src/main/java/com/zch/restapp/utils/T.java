package com.zch.restapp.utils;

import android.content.Context;
import android.widget.Toast;

public class T {
    private static Toast toast;

    public static void showToast(String content){
        toast.setText(content);
        toast.show();
    }

    public static void init(Context context){
        toast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
    }
}
