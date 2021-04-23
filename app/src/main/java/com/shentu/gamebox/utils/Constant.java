package com.shentu.gamebox.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.annotation.Keep;
import androidx.annotation.StringRes;
import androidx.core.content.res.ResourcesCompat;

import com.shentu.gamebox.R;
import com.shentu.gamebox.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

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
    public static String CHECK_VERSION = "game_box/api/check_version";

    /*代理code*/

    public static String GAME_ID = "8";

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

}
