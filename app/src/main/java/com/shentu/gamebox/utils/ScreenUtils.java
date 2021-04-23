package com.shentu.gamebox.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;


public class ScreenUtils {

    public static int getScreenWidth(Context context){
        if(context == null){
            return 0;
        }
      WindowManager wm =   (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
    public static int dp2px(Context context,float dpval){
      final float scale = context.getResources().getDisplayMetrics().density;
      return (int)(dpval *scale + 0.5f);
    }
}
