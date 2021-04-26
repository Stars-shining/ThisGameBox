package com.shentu.gamebox.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.shentu.gamebox.R;
import com.shentu.gamebox.adapter.TabFragmentAdapter;
import com.shentu.gamebox.adapter.mViewPagerAdapter;
import com.shentu.gamebox.adapter.myTabViewAdapter;
import com.shentu.gamebox.bean.BannerBean;
import com.shentu.gamebox.bean.DetailBean;
import com.shentu.gamebox.bean.DetialGameBean;
import com.shentu.gamebox.bean.DownLoadBean;
import com.shentu.gamebox.bean.HomeItem;
import com.shentu.gamebox.bean.HttpResult;
import com.shentu.gamebox.bean.VipBean;
import com.shentu.gamebox.http.ApiException;
import com.shentu.gamebox.http.RetrofitManager;

import com.shentu.gamebox.http.UpdateVersion;
import com.shentu.gamebox.plugin.PluginManager;

import com.shentu.gamebox.utils.Constant;
import com.shentu.gamebox.utils.DialogUtils;
import com.shentu.gamebox.utils.FieldMapUtils;
import com.shentu.gamebox.utils.Indicator;
import com.shentu.gamebox.utils.LogUtils;
import com.shentu.gamebox.utils.SharePreferenceUtil;
import com.shentu.gamebox.view.CustomProgress;
import com.shentu.gamebox.base.BaseFragment;
import com.shentu.gamebox.view.MVideoController;
import com.shentu.gamebox.view.widget.MmediaController;
import com.shentu.gamebox.view.widget.VideoView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jzvd.JZTextureView;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GameFragment extends BaseFragment implements View.OnClickListener, Html.ImageGetter {

    private ViewPager viewpager;
    private TabLayout tablayout;
    private ArrayList<Fragment> fragments;
    private static final String tag = "args";
    private String mTag;
    private boolean needIntercept = false;
    int mLastXIntercept = 0;
    int mLastYIntercept = 0;
    private CustomProgress down_prograss;
    private Handler mHandler;
    private int currentProgress;
    private Button btn_download;
    private JzvdStd videoView;
    private ImageView xq_game_img;
    private TextView xq_game_title;
    private TextView xq_game_rec;
    private TextView game_content;
    private String gameId;
    private String path3;
    private HomeItem homeItem;
    private BannerBean bannerBean;
    /*福利 返现*/
    private TextView reback_btn;
    private TextView gift_btn;
    private String agent_code;
    private Disposable mDisposable;
    private String type;
    private DetialGameBean detialGameBean;
    private String gift_detail;
    private ImageView image_view;
    private List<String> imgsList;
    private TextView scan_more;
    private long downloadLength;
    private long contentLength;
    private myTabViewAdapter tabAdapter;
    private TabFragmentAdapter tabFragmentAdapter;
    private int maxline = 5;
//    private MmediaController mmediaController;
    private RelativeLayout video_view_layout;
    private ImageView play_img;
    private String url;
    private String apkname;
    private FrameLayout fragment_tab1;
    private FrameLayout fragment_tab2;
    private FrameLayout fragment_tab3;

    private boolean playWhenReady = true;
    private int currentWindow;
    private long playbackPosition;
    private ImageView exo_fullscreen_button;


    @Override
    protected int setView() {
        return R.layout.xiangqing;
    }

    @Override
    protected void initView(View view) {

        videoView = view.findViewById(R.id.video_view);
        fragment_tab1 = view.findViewById(R.id.fragment_tab1);
        fragment_tab2 = view.findViewById(R.id.fragment_tab2);
        fragment_tab3 = view.findViewById(R.id.fragment_tab3);
        video_view_layout = view.findViewById(R.id.video_view_layout);

        image_view = view.findViewById(R.id.detail_image_view);

        down_prograss = view.findViewById(R.id.down_prograss);
        btn_download = view.findViewById(R.id.game_download);
        viewpager = view.findViewById(R.id.gamepg_viewpg);
        tablayout = view.findViewById(R.id.gamepg_tab);
        down_prograss.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.prograss_line, null));

        xq_game_img = view.findViewById(R.id.xq_game_img);
        xq_game_title = view.findViewById(R.id.xq_game_title);
        xq_game_rec = view.findViewById(R.id.xq_game_rec);

        reback_btn = view.findViewById(R.id.reback_btn);
        gift_btn = view.findViewById(R.id.gift_btn);


        game_content = view.findViewById(R.id.game_content);
        scan_more = view.findViewById(R.id.scan_more);

        gift_btn.setOnClickListener(this);
        reback_btn.setOnClickListener(this);
        scan_more.setOnClickListener(this);
        btn_download.setOnClickListener(this);
        btn_download.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.gift_btn, null));

    }


    public static Fragment newInstance(String param) {
        PriceFragment fragment = new PriceFragment();
        Bundle args = new Bundle();
        args.putString(tag, param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*接收MainActivity 发送的数据*/
        EventBus.getDefault().register(this);

        if (getArguments() != null) {
            mTag = getArguments().getString(tag);
        }
//        mHandler = new ProHandler(mActivity);
    }

    @Override
    protected void initData() {
        Constant constant = new Constant(mActivity);
        agent_code = constant.getAgentCode();
//        Tabadd();
        requestDetail();
        /*查看游戏是否安装*/
        String pkName = (String) SharePreferenceUtil.getParam(mActivity, gameId, "");
        if (null != pkName && !pkName.isEmpty()) {
            btn_download.setText("打开");
        }



    }

    public void videoPlayer(String uri, String cover) {
        videoView.setUp(uri,"", Jzvd.SCREEN_NORMAL);
        JzvdStd.SAVE_PROGRESS = false;
        Glide.with(mActivity).load(cover).into(videoView.posterImageView);
        videoView.startVideo();
        LogUtils.e("播放视频");
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @SuppressLint("NewApi")
    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }


    @Override
    public Drawable getDrawable(String source) {

        return null;
    }

    /*MainActivity 传递过来的信息*/
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getItemDetial(Bundle bundle) {
        homeItem = (HomeItem) bundle.getSerializable("homeItem");
        bannerBean = (BannerBean) bundle.getSerializable("bannerBean");
        if (bannerBean != null) {
            gameId = bannerBean.getGame_id();
        } else {
            gameId = homeItem.getId();
        }
        type = String.valueOf(bundle.getInt("type"));
        LogUtils.e(type + "");
//        bannerBean = (BannerBean) bundle.getSerializable("bannerBean");

    }

    /*详情页*/
    public void requestDetail() {

        HashMap<String, Object> map = FieldMapUtils.getRequestBody("", gameId, agent_code, Constant.GAME_DETAIL, "");

        RetrofitManager.getInstance().GameDetailInfo(new Observer<HttpResult<DetailBean>>() {


            private String openClose;
            private String vip;
            private String beanImgs;
            private List<VipBean> vipBeans;


            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull HttpResult<DetailBean> gameBeanHttpResult) {
                detialGameBean = gameBeanHttpResult.getData().getDetial();
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                ApiException apiException = (ApiException) e;
                LogUtils.e(apiException.getCode() + apiException.getDispalyMessage());
            }

            @Override
            public void onComplete() {

                RoundedCorners roundedCorners = new RoundedCorners(10);
                RequestOptions override = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);

                if (detialGameBean != null) {
                    /*福利地址*/
                    gift_detail = detialGameBean.getGift_detail();
                    String name = detialGameBean.getName();
                    beanImgs = detialGameBean.getImgs();
                    String cover = detialGameBean.getCover();
                    String intro = detialGameBean.getIntro();
                    String version_intro = detialGameBean.getVersion_intro();
                    String icon = detialGameBean.getIcon();
                    String videoUri = detialGameBean.getVideo();
                    String beanRebate = detialGameBean.getRebate();
                    vip = detialGameBean.getVip_open();
                    openClose = detialGameBean.getOpen_close();
                    String rebate = detialGameBean.getRebate();
                    if (!vip.isEmpty()) {
                        vipBeans = JSONObject.parseArray(vip, VipBean.class);
                    }
                    xq_game_title.setText(name);
                    Glide.with(mActivity).load(icon).into(xq_game_img);
                    xq_game_rec.setText(intro);
                    game_content.setText(version_intro);



                    game_content.setHeight(game_content.getLineHeight() * maxline);
                    game_content.post(new Runnable() {
                        @Override
                        public void run() {
                            scan_more.setVisibility(game_content.getLineCount() > maxline ? View.VISIBLE : View.GONE);
                        }
                    });
                    if (!videoUri.isEmpty()) {
//
                                videoPlayer(videoUri,cover);
                                image_view.setVisibility(View.GONE);

                    } else {

                        imgsList = JSONArray.parseArray(beanImgs, String.class);
                        image_view.setVisibility(View.VISIBLE);
                        videoView.setVisibility(View.GONE);
                        LogUtils.e(cover);
                        if (cover.isEmpty() && imgsList.size() != 0) {
                            Glide.with(mActivity).load(imgsList.get(0)).apply(override).into(image_view);
                        } else {
                            Glide.with(mActivity).load(cover).into(image_view);
                        }
                    }
                    if (!beanRebate.equals("0")) {

                        reback_btn.setVisibility(View.VISIBLE);
                        reback_btn.setText("现金返利" + rebate + "%");
//                        LogUtils.e(detialGameBean.toString());
                        LogUtils.e(detialGameBean.getGift_detail());
                        LogUtils.e(detialGameBean.getRebate());
                    }

                    if (!gift_detail.isEmpty() && !gift_detail.equals("0")) {
                        gift_btn.setVisibility(View.VISIBLE);
                    }

//                    videoView.setVideoURI(Uri.parse(path3));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("detialGameBean", detialGameBean);


                    /*eventbus发送数据*/
                    EventBus.getDefault().postSticky(detialGameBean);

                    /*初始化地步tab和viewpager+fragment*/
                    initViewPager(beanImgs, vipBeans.get(0), openClose);
                }
            }
        }, map);
    }


    private void initViewPager(String beanImgs, VipBean vip, String open) {

        Fragment openareaFragment = OpenAreaFragment.newInstance("open");
        Fragment priceFragment = PriceFragment.newInstance("price");
        Fragment imageFragment = ImageFragment.newInstance("image");

//        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.tab_fragment,openareaFragment);
//        transaction.commit();
        ArrayList<String> strtab = new ArrayList<>();
        fragments = new ArrayList<>();
        strtab.clear();
        if (!beanImgs.isEmpty()) {
            strtab.add("游戏截图");
            tablayout.addTab(tablayout.newTab().setText("游戏截图"));
            fragments.add(imageFragment);
        }
        if (!vip.getLavel().isEmpty()) {
            strtab.add("VIP价格列表");
            tablayout.addTab(tablayout.newTab().setText("VIP价格列表"));
            fragments.add(priceFragment);
        }
        if (!open.isEmpty()) {
            strtab.add("开合区介绍");
            tablayout.addTab(tablayout.newTab().setText("开合区介绍"));
            fragments.add(openareaFragment);
        }



        FragmentTransaction fm = getChildFragmentManager().beginTransaction();
        fm.add(R.id.fragment_tab1, imageFragment);
        fm.add(R.id.fragment_tab2, priceFragment);
        fm.add(R.id.fragment_tab3, openareaFragment);
        fm.commit();
        TabLayout.Tab tabAt = tablayout.getTabAt(0);

//        fm.hide(imageFragment);
//        fm.hide(priceFragment);
//        fm.hide(openareaFragment);

        if (tabAt.isSelected()&& tabAt.getText().equals("游戏截图")){

            fragment_tab1.setVisibility(View.VISIBLE);
            fragment_tab2.setVisibility(View.GONE);
            fragment_tab3.setVisibility(View.GONE);

        }
        if (tabAt.isSelected()&& tabAt.getText().equals("VIP价格列表")){
            fragment_tab1.setVisibility(View.GONE);
            fragment_tab2.setVisibility(View.VISIBLE);
            fragment_tab3.setVisibility(View.GONE);
        }
        if (tabAt.isSelected()&& tabAt.getText().equals("开合区介绍")){
            fragment_tab1.setVisibility(View.GONE);
            fragment_tab2.setVisibility(View.GONE);
            fragment_tab3.setVisibility(View.VISIBLE);
        }

//        tabFragmentAdapter = new TabFragmentAdapter(mActivity.getSupportFragmentManager(), getLifecycle(), fragments);
//        FragmentManager fm =getChildFragmentManager();
//        mViewPagerAdapter mViewPagerAdapter = new mViewPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,fragments);
//        viewpager.setAdapter(mViewPagerAdapter);
//        viewpager.setOffscreenPageLimit(fragments.size());
//        viewpager.setCurrentItem(0);
//        tablayout.setupWithViewPager(viewpager);
//        new TabLayoutMediator(tablayout, viewpager, new TabLayoutMediator.TabConfigurationStrategy() {
//            @Override
//            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                tab.setText(strtab.get(position));
//            }
//        }).attach();

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("游戏截图")){
                    fragment_tab1.setVisibility(View.VISIBLE);
                    fragment_tab2.setVisibility(View.GONE);
                    fragment_tab3.setVisibility(View.GONE);
                }
                 if (tab.getText().equals("VIP价格列表")){
                     fragment_tab1.setVisibility(View.GONE);
                     fragment_tab2.setVisibility(View.VISIBLE);
                     fragment_tab3.setVisibility(View.GONE);
                }
                 if (tab.getText().equals("开合区介绍")){
                     fragment_tab1.setVisibility(View.GONE);
                     fragment_tab2.setVisibility(View.GONE);
                     fragment_tab3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getText().equals("游戏截图")){
                    fragment_tab1.setVisibility(View.GONE);

//                    fm.hide(imageFragment);
                }
                if (tab.getText().equals("VIP价格列表")){
                    fragment_tab2.setVisibility(View.GONE);

//                    fm.hide(priceFragment);
                }
                if (tab.getText().equals("开合区介绍")){
                    fragment_tab3.setVisibility(View.GONE);
//                    fm.hide(openareaFragment);
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        tablayout.post(new Runnable() {
//            @Override
//            public void run() {
//                /*设置tab指示条样式*/
//                Indicator.setIndicator(tablayout, 13, 13, ContextCompat.getColor(mActivity, R.color.btnBg));
//            }
//        });
    }

    @Override
    public void onClick(View v) {

        int startHeight = game_content.getLineHeight();
        switch (v.getId()) {
            //下载游戏 展示进度条
            case R.id.game_download:

                if (btn_download.getText() != null && btn_download.getText().equals("打开")) {
                    //进行跳转
                    String pkName = (String) SharePreferenceUtil.getParam(mActivity, "gameId", "");

                    Intent aPackage = mActivity.getPackageManager().getLaunchIntentForPackage(pkName);
                    String className = aPackage.getComponent().getClassName();


                    LogUtils.e("packageName" + pkName + "activityName" + className);
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(new ComponentName(pkName, className));
                    startActivity(intent);

//                    OpenActivity();
                } else {
                    Toast.makeText(mActivity, "下载中", Toast.LENGTH_SHORT).show();
                    btn_download.setVisibility(View.GONE);
                    down_prograss.setVisibility(View.VISIBLE);

                    requestDownUrl();
                }
                break;
            /*返利*/
            case R.id.reback_btn:
                showGiftHtml(detialGameBean.getRebate_detail());
                break;
            /*福利*/
            case R.id.gift_btn:

                showGiftHtml(detialGameBean.getGift_detail());
                break;
            case R.id.scan_more:
                int lineHeight = game_content.getLineHeight();
                int lineCount = game_content.getLineCount();
                if (scan_more.getText().equals("更多")) {

                    game_content.setHeight(lineHeight * lineCount);
                    scan_more.setText("收起");
                } else {

                    game_content.setHeight(maxline * lineHeight);
                    scan_more.setText("更多");
                }

                break;
        }
    }



    private void storeApkInfo(File file) {
        String packageName;
        String activityName;
        PackageInfo packageInfo = null;

        try {
            packageInfo = mActivity.getPackageManager().getPackageInfo(file.getPath(), PackageManager.GET_ACTIVITIES);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        packageName = packageInfo.packageName;
        /**/
        SharePreferenceUtil.setParam(mActivity, packageName, gameId);

        LogUtils.e("packageName" + packageName  );

    }

    private void showGiftHtml(String htmlDate) {
        if (!htmlDate.isEmpty()){

            View view = LayoutInflater.from(mActivity).inflate(R.layout.activity_webview, null);
            DialogUtils.showHtmlDialog(mActivity, view, htmlDate);
        }else{
            Toast.makeText(mActivity, "很抱歉，暂无内容！", Toast.LENGTH_SHORT).show();
        }

    }

    public String getHtmlDate(String bodyHTML) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    public void showGiftImage() {
        Uri uri = null;
        LogUtils.e(gift_detail);
        Intent intent = new Intent(mActivity, WebViewActivity.class);

        if (!gift_detail.isEmpty() && gift_detail.contains("src")) {
            Matcher matcher = Pattern.compile("\"http?(.*?)(\"|\\s+)").matcher(gift_detail);
            boolean b = matcher.find();
            if (b) {
                String group = matcher.group();
                LogUtils.e(group);
                String substring = group.substring(1, group.length() - 1);
                uri = Uri.parse(substring);

            }
        } else if (!gift_detail.isEmpty()) {

            uri = Uri.parse(gift_detail);

        }
        if (uri != null) {

            Bundle bundle = new Bundle();
            bundle.putString("uri", String.valueOf(uri));
            intent.putExtras(bundle);
            startActivity(intent);
        } else {

            Bundle bundle = new Bundle();
            bundle.putString("uri", gift_detail);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private boolean isValuable(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        /*获取所有自己安装包*/
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        List<String> pNames = new ArrayList<>();
        if (installedPackages != null) {
            for (PackageInfo info : installedPackages) {
                pNames.add(info.packageName);
            }
        }
        return pNames.contains(packageName);
    }

    /*下载游戏apk*/
    public void requestDownUrl() {

        HashMap<String, Object> map = FieldMapUtils.getRequestBody(type, gameId, agent_code, Constant.DOWN_LOAD, "");

        RetrofitManager.getInstance().DownLoadGame(new Observer<HttpResult<DownLoadBean>>() {


            private String msg;
            private DownLoadBean resultData;

            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull HttpResult<DownLoadBean> loadBeanHttpResult) {
                msg = loadBeanHttpResult.getMsg();
                url = loadBeanHttpResult.getData().getString();

            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//                ApiException apiException = (ApiException) e;
//                LogUtils.e(apiException.getCode() + apiException.getDispalyMessage());
//                com.jakewharton.retrofit2.adapter.rxjava2.HttpException cannot be cast to
            }

            @Override
            public void onComplete() {

                LogUtils.e(url);
                if (!url.isEmpty()) {
                    SharePreferenceUtil.setParam(mActivity, url, "url");
                    downFile(url);
                } else {
                    Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                }


//                UpdateVersion updateVersion = new UpdateVersion(mActivity);
//                if (url != null) {
//                    updateVersion.downFile(url);
//                }
            }
        }, map);
    }

    /*下载文件*/
    public void downFile(String url) {
        UpdateVersion updateVersion = new UpdateVersion(mActivity);
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Integer> emitter) throws Exception {
                downApk(url, emitter);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Integer integer) {
                        //设置progressdialog 进度条进度

                        down_prograss.setProgress(integer);
                        down_prograss.incrementProgressBy((int) (downloadLength * 1.0f / contentLength * 100));
//                        if (contentLength == downloadLength) {
//                            btn_download.setVisibility(View.VISIBLE);
//                            down_prograss.setVisibility(View.GONE);
//                            btn_download.setText("打开");
//                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Toast.makeText(mActivity, "网络异常，请重新下载！", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /*下载apk*/
    private void downApk(String url, ObservableEmitter<Integer> emitter) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //下载失败 断点续传
                downApk(url, emitter);
//                breakPoint(url, emitter);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body() == null) {
                    //下载失败
//                    breakPoint(url, emitter);
                    downApk(url, emitter);
                }

                InputStream is = null;
                FileOutputStream fos = null;
                int len;
                try {
                    is = Objects.requireNonNull(response.body()).byteStream();
                    String[] apks = "/".split(url);
                    apkname = apks[apks.length - 1];
                    File file = createFile(apkname);
                    fos = new FileOutputStream(file);
                    byte[] bytes = new byte[2048];
                    long total = Objects.requireNonNull(response.body()).contentLength();
                    contentLength = total;
                    long sum = 0;
                    while ((len = is.read(bytes)) != -1) {
                        fos.write(bytes, 0, len);
                        sum += len;
                        int progress = (int) (len * 1.0f / total * 100);
                        /*下载中 更新进度*/
                        emitter.onNext(progress);
                        downloadLength = sum;
                    }
                    fos.flush();
                    LogUtils.e("下载完成");

                    /*获取apk包名 类名*/
//                    storeApkInfo(file);
                    //下载完成 安装apk
                    UpdateVersion.installApk(mActivity, file);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (is != null)
                        is.close();
                    if (fos != null)
                        fos.close();
                }
            }
        });
    }

    private File createFile(String apkname) {
        String path = mActivity.getExternalFilesDir("dir").getPath();
        File file = new File(path, apkname);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*判断是否选择文件 */
        if (null != data) {
            String uriPath = getUriPath(data.getData());
            Toast.makeText(mActivity, uriPath, Toast.LENGTH_SHORT).show();
        } else {

        }

    }

    public String getUriPath(Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            try {
                Cursor query = mActivity.getContentResolver().query(uri, projection, null, null, null);
                int columnIndex = query.getColumnIndexOrThrow("_data");
                if (query.moveToNext()) {
                    return query.getString(columnIndex);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /*解注册*/
        EventBus.getDefault().unregister(this);
        /*Rxjava解除订阅*/
        mDisposable.dispose();

    }
}
