package com.shentu.gamebox.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

public class ChangeAPkNameIcon {

    private final Context mContext;

    public ChangeAPkNameIcon(Context context) {
        mContext = context;
    }

    /*别名myname对应的 classname = getpackageName()+name*/
    String componentName = "com.shentu.gamebox.myName";

    public void getName (){

        ComponentName icon_tag = new ComponentName(mContext, this.componentName);
        PackageManager manager = mContext.getPackageManager();
//        ComponentName currentName =  mContext.getComponentName();
//        if (currentName.getClassName().equals(componentName)){
//            manager.setComponentEnabledSetting(icon_tag,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
//        }else{
//            manager.setComponentEnabledSetting(icon_tag,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
//        }
    }
}
