package com.shentu.gamebox.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.shentu.gamebox.R;

import com.shentu.gamebox.base.BaseActivity;
import com.shentu.gamebox.bean.BannerBean;
import com.shentu.gamebox.bean.HomeItem;

import com.shentu.gamebox.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


public class GameDetialActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout layout_gamedesc;
    public String gameId;
    private ImageView back;
    private HomeItem homeItem;
    private BannerBean bannerBean;
    private String type;
    private Bundle bundleMain;
    private int volume;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }


    @Override
    protected void initView() {
        layout_gamedesc = findViewById(R.id.gamedesc_layout);
        back = findViewById(R.id.back_home);

        back.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        GameFragment gameFragment = new GameFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.gamedesc_layout, gameFragment)
                .commit();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getAction()) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:

                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                FragmentManager manager = getSupportFragmentManager();
                GameFragment gameFragment = (GameFragment) manager.findFragmentByTag("gameFragment");
                JzvdStd videoView = gameFragment.getVideoView();
                LogUtils.e("555555");
                if (videoView != null)
                videoView.showVolumeDialog(20,volume);
                videoView.dismissVolumeDialog();
                break;

            case KeyEvent.KEYCODE_VOLUME_MUTE:
                break;

        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.layout_gamedes;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_home:
                finish();
                LogUtils.e("点击返回主页面");
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }

        super.onBackPressed();
    }
}
