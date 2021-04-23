package com.shentu.gamebox.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.shentu.gamebox.R;
import com.shentu.gamebox.base.BaseActivity;
import com.shentu.gamebox.ui.MainActivity;

import java.lang.ref.WeakReference;

public class SetStatusBarColor {

    private Activity mActivity;

    public SetStatusBarColor(BaseActivity activity) {
        mActivity = activity;
    }

    /*设置状态栏颜色*/
    public void setStatueBarColor(int color) {

//        View view = new View(MainActivity.this);
//        view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
//        view.getLayoutParams().height = getStatuBarHeight();
//
//
//
        Window window = mActivity.getWindow();
//        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        ViewGroup decorView = (ViewGroup) window.getDecorView();
//        int indexOfChild = decorView.indexOfChild(view);
//        /*底部不停的点击 防止view被重复添加*/
//        if (indexOfChild == -1){
//            decorView.addView(view);
//        }else{
//            view = decorView.getChildAt(indexOfChild);
//        }
//        view.setBackgroundColor(color);


        /*5.0到6.0修改状态栏颜色*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /*取消设置透明状态栏 使contentView 内容不在覆盖状态栏*/
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            /*设置这个flags 才能调用 setStatusBarColor 来设置状态栏颜色*/
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            /*设置状态栏颜色*/
            window.setStatusBarColor(color);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (color == ContextCompat.getColor(mActivity, R.color.transparent_background)) {
                /*状态栏图标文字深色 同时隐藏状态栏*/
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            } else {
                /*状态栏图标文字浅色 同时隐藏状态栏*/
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            }
        }
    }

    private int getStatuBarHeight() {
        int dimensionPixelSize = 0;
        Resources res = mActivity.getResources();
        int identifier = res.getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            dimensionPixelSize = res.getDimensionPixelSize(identifier);
        }
        return dimensionPixelSize;
    }
}
