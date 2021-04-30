package com.shentu.gamebox.base;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.shentu.gamebox.R;
import com.shentu.gamebox.utils.CrashHandlerUtils;
import com.shentu.gamebox.utils.SetStatusBarColor;

public abstract class BaseActivity  extends AppCompatActivity {

    private BaseApplication application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayoutId());
        /*崩溃信息搜集*/
//        CrashHandlerUtil instance = CrashHandlerUtil.getInstance();
//        instance.init(this);
//        String crashTip = instance.getCrashTip();


        CrashHandlerUtils.getInstance().init(this);
        /*设置状态栏*/
        SetStatusBarColor barColor = new SetStatusBarColor(this);
        barColor.setStatueBarColor(ContextCompat.getColor(this,R.color.transparent_background));
        initView();

        initData();
    }

    /*初始化view*/
    protected abstract void initView();
    /*布局id*/
    protected abstract int initLayoutId();
    /*初始化data*/
    protected abstract void initData();

    /**
     * 全屏  和 不是全屏  显示：
     */
    public void fullScreen(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

}
