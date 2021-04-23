package com.shentu.gamebox.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.checkSelfPermission;

@Keep
public class Permission {

    public static int mRequestCode;
    public static Activity activity;

    public Permission(Activity activity) {

        this.activity = activity;
//        //一般是在触发某个事件的时候再请求动态权限，比如点击按钮跳转到一个拍照页面，如果权限通过就跳转，否者吐司说没有权限！
//        if (Build.VERSION.SDK_INT >= 23) {//6.0才用动态权限
//            initPermission(activity);
//        }
    }

        //权限判断和申请
        public static void initPermission (){

            //1、首先声明一个数组permissions，将需要的权限都放在里面
             String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
            //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
           List<String> mPermissionList = new ArrayList<>();

            //权限请求码
            mRequestCode = 100;
            mPermissionList.clear();//清空没有通过的权限

            //逐个判断你要的权限是否已经通过
            for (int i = 0; i < permissions.length; i++) {
                if (checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);//添加还未授予的权限
                }
            }

            //申请权限
            if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
                ActivityCompat.requestPermissions(activity, permissions, mRequestCode);
            } else {
                //说明权限都已经通过，可以做你想做的事情去
            }
        }


        //请求权限后回调的方法
        //参数： requestCode  自己定义的权限请求码
        //参数： permissions  请求的权限名称数组
        //参数： grantResults 在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权

        public  void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (mRequestCode == requestCode) {
                boolean hasPermissionDismiss = false;//有权限没有通过
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == -1) {
                        hasPermissionDismiss = true;
                    }
                }
                //如果有权限没有被允许
                if (hasPermissionDismiss) {
                    showPermissionDialog();//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
                } else {
                    //全部权限通过，可以进行下一步操作。。。

                }
            }

        }


        /**
         * 不再提示权限时的展示对话框
         */
        static AlertDialog mPermissionDialog;
       static String mPackName = "com.shentu.gamebox";

        public static void showPermissionDialog(){

                mPermissionDialog = new AlertDialog.Builder(activity)
                        .setMessage("已禁用权限，请手动授予")
                        .setCancelable(false)
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelPermissionDialog();

                                Uri packageURI = Uri.parse("package:" + mPackName);
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                activity.startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //关闭页面或者做其他操作
                                cancelPermissionDialog();
                                activity.finish();

                            }
                        })
                        .create();

            mPermissionDialog.show();
        }

        //关闭对话框
        private static void cancelPermissionDialog() {
            mPermissionDialog.cancel();
        }
    /**
     * 判断定位服务是否开启
     *
     * @param
     * @return true 表示开启
     */
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }


    /**
     * 提示用户去开启定位服务
     **/
    public static void toOpenGPS(final Context activity) {
        new androidx.appcompat.app.AlertDialog.Builder(activity).setTitle("提示").setMessage("手机定位服务未开启，无法获取到您的准确位置信息，是否前往开启？").setNegativeButton("取消", null).setPositiveButton("去开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(intent);
                dialogInterface.dismiss();
            }
        }).show();
    }


}
