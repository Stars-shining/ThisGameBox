package com.shentu.gamebox.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.StringRes;
import androidx.core.content.res.ResourcesCompat;

import com.shentu.gamebox.R;
import com.shentu.gamebox.base.BaseActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

@Keep
public class Constant {

    private Context mContext;

    public Constant(Context context) {
        mContext = context;
    }
    /*测试服*/
//    public static String testUrl = "http://forward.88h5.cn";

    public static String normalUrl = "http://forward.hzbaoliandeng.com";

    public static String GAME_DETAIL = "game_box/api/game_detail";
    public static String GET_GAMES = "game_box/api/get_games";
    public static String GET_CONTACT = "game_box/api/get_box_info";
    public static String GET_BANNER = "game_box/api/get_banner";
    public static String DOWN_LOAD = "game_box/api/download";
    public static String GAME_COUNT = "game_box/api/game_action";
    public static String INSTALL_COUNT = "game_box/api/install";
    public static String LAUNCH_COUNT = "game_box/api/launch";
    public static String CHECK_VERSION = "game_box/api/check_version";
    public static boolean INSTALLED = false;
    /*uuid。txt 文件*/
    String fileName = "UUID.txt";
    /*代理code*/


    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return format.format(System.currentTimeMillis());
    }

    public String getAgentCode() {

        return mContext.getResources().getString(R.string.agent_code);

    }


    public String getMetaDateFromApp() {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = mContext.getPackageManager().getApplicationInfo(
                    mContext.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert applicationInfo != null;
        return applicationInfo.metaData.getString("agent_version");
    }

    public int getMetaDataFromActivity() {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = mContext.getPackageManager().getApplicationInfo(
                    mContext.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert applicationInfo != null;
        return applicationInfo.metaData.getInt("");
    }
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public  void saveUniqueID()  {
        /*获取内部储存状态*/
        String state = Environment.getExternalStorageState();
        /*如果不是mounted 不可续写*/
        if (state.equals(Environment.MEDIA_MOUNTED));{
            String absolutePath = mContext.getExternalFilesDir("CaChe").getAbsolutePath();
            File uidFile = new File(absolutePath,fileName);
            if (!uidFile.exists()){
                try {
                    uidFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //保存uuid
            try {
                FileWriter fw = new FileWriter(uidFile);
                fw.write(getUUID());
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public  String readUUId(){
        BufferedReader bf = null;
        StringBuffer buffer = null;
        try {
            String absolutePath = mContext.getExternalFilesDir("CaChe").getAbsolutePath();
            File uidFile = new File(absolutePath,fileName);
            FileReader fileReader = new FileReader(uidFile);
             buffer = new StringBuffer();
             bf = new BufferedReader(fileReader);
            String line;
            while ((line = bf.readLine() )!= null){
                buffer.append(line);
            }
           
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bf != null){
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        assert buffer != null;
        return buffer.toString();
    }
    /*获取app包名*/
    public String getApkInfo(File file){
        PackageManager pm = mContext.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(file.getPath(), PackageManager.GET_ACTIVITIES);
        if (packageInfo != null){
            return     packageInfo.applicationInfo.packageName;
        }
        return null;
    }

    public void startApp (String packageName){
        try{
            Intent aPackage = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
            mContext.startActivity(aPackage);
        }catch (Exception e){
            INSTALLED = false;
            Toast.makeText(mContext, "请先安装app", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isAppInstalled(String pkName){
        PackageManager packageManager = mContext.getPackageManager();
        boolean installed = false;
        try {
             packageManager.getPackageInfo(pkName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            installed = false;
        }
        return installed;
    }
}
