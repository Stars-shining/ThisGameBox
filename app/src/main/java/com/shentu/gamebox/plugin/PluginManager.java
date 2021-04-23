package com.shentu.gamebox.plugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PluginManager {
    private static PluginManager mInstance = new PluginManager();
    private Context context;
    private AssetManager assetManager;
    private DexClassLoader dexClassLoader;
    private PackageInfo packageArchiveInfo;
    private Resources pluginResource;

    public static PluginManager getInstance(){
         return mInstance;
     }

    public PluginManager() {
    }

    public void setContext(Context context){
        this.context = context.getApplicationContext();
    }


    public void loadApk(String dexPath){

        dexClassLoader = new DexClassLoader(dexPath, context.getDir("file", Context.MODE_PRIVATE).getAbsolutePath(), null, context.getClassLoader());

        try {
            packageArchiveInfo = context.getPackageManager().getPackageInfo(dexPath, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            assetManager = AssetManager.class.newInstance();
            Method assetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            assetPath.invoke(assetManager,dexPath);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        pluginResource = new Resources(assetManager,context.getResources().getDisplayMetrics(),context.getResources().getConfiguration());

    }

    public DexClassLoader getPluginDexClassLoader(){
        return dexClassLoader;
    }

    public Resources getPluginResource(){
        return pluginResource;
    }

    public PackageInfo getPackageArchiveInfo(){
        return packageArchiveInfo;
    }

}
