package com.shentu.gamebox.base;

import android.app.Application;
import android.content.Context;


import androidx.multidex.MultiDex;


import com.shentu.gamebox.http.RetrofitManager;

public class BaseApplication extends Application {

    //全局唯一的context
    private static BaseApplication application;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        application = this;
        //MultiDexf分包初始化，必须最先初始化
        MultiDex.install(this);
        RetrofitManager.getInstance().init();

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }



    /**
     * 获取全局唯一上下文
     *
     * @return BaseApplication
     */
    public static BaseApplication getApplication() {
        return application;
    }
}

