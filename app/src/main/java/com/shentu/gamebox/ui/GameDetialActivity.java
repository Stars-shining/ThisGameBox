package com.shentu.gamebox.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;


import com.shentu.gamebox.R;

import com.shentu.gamebox.base.BaseActivity;
import com.shentu.gamebox.bean.BannerBean;
import com.shentu.gamebox.bean.HomeItem;
import com.shentu.gamebox.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;


public class GameDetialActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout layout_gamedesc;
    public String gameId;
    private ImageView back;
    private HomeItem homeItem;
    private BannerBean bannerBean;
    private String type;
    private Bundle bundleMain;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void setTitle() {
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
//        assert actionBar != null;
//        actionBar.setDisplayShowCustomEnabled(true);
//        View view = LayoutInflater.from(this).inflate(R.layout.action_title, null);
//        TextView title = view.findViewById(R.id.detial_title);
//        back = view.findViewById(R.id.back_home);
//        title.setText("游戏详情");
//        actionBar.setCustomView(view,new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT
//        ,ActionBar.LayoutParams.MATCH_PARENT));

//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    @Override
    protected void initView() {
        layout_gamedesc = findViewById(R.id.gamedesc_layout);
        back = findViewById(R.id.back_home);

        back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("homeItem", homeItem);
//        bundle.putSerializable("bannerBean", bannerBean);
//        bundle.putString("type",type);
//        /*发送 banner 和 homeItem 对象*/
//        EventBus.getDefault().postSticky(bundleMain);
        GameFragment gameFragment = new GameFragment();
//        gameFragment.setArguments(bundleMain);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.gamedesc_layout, gameFragment)
                .commit();

    }

    @Override
    protected int initLayoutId() {
        return R.layout.layout_gamedes;
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
}
