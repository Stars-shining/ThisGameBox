package com.shentu.gamebox.utils;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
/*自定义指示条样式*/
public class Indicator {

    private static Field slidingTabIndicator;
    private static Field field;

    public static void  setIndicator(TabLayout tabs, int leftDip, int rightDip, int color) {
        try {

//            slidingTabIndicator = tabs.getClass().getDeclaredField("slidingTabIndicator");
            if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.P){

                slidingTabIndicator = tabs.getClass().getDeclaredField("slidingTabIndicator");
            }else{
                slidingTabIndicator = tabs.getClass().getDeclaredField("mTabStrip");
            }
            slidingTabIndicator.setAccessible(true);
            LinearLayout mTabStrip = (LinearLayout) slidingTabIndicator.get(tabs);
            int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
            int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
            for (int i =0;i<mTabStrip.getChildCount();i++){
                View tabView = mTabStrip.getChildAt(i);
                //tabview的mtextview属性
                if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.P){
                    field = tabView.getClass().getDeclaredField("textView");
                }else {
                    field = tabs.getClass().getDeclaredField("mTextView");
                }
                field.setAccessible(true);
                TextView mTextView = (TextView) field.get(tabView);
                mTextView.setHintTextColor(Color.BLUE);
                mTextView.setLinkTextColor(Color.BLACK);
                tabView.setPadding(0,0,0,0);
//                //想要字多宽线就多宽 测量mTextview的宽度
//                int width = mTextView.getWidth();
//                if (width ==0){
//                    mTextView.measure(0,0);
//                }

                tabView.setPadding(0,0,0,0);
                //设置tab左右间距 不能用paddiing 源码宽度是根据tabview的宽度来设置的、
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
//                params.width = width;
                params.rightMargin = left;
                params.leftMargin = right;
                tabView.setLayoutParams(params);
                tabView.invalidate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
