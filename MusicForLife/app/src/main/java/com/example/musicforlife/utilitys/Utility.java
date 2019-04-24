package com.example.musicforlife.utilitys;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

public class Utility {
//    private static  Activity  mActivity;
    public Utility(){

    }
    public static void setTransparentStatusBar(Activity activity){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            Window window= activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
}
