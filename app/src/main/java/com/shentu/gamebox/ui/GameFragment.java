package com.shentu.gamebox.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import com.google.android.material.tabs.TabLayout;
import com.shentu.gamebox.R;

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

import com.shentu.gamebox.utils.Constant;
import com.shentu.gamebox.utils.DialogUtils;
import com.shentu.gamebox.utils.FieldMapUtils;
import com.shentu.gamebox.utils.LogUtils;
import com.shentu.gamebox.utils.SharePreferenceUtil;
import com.shentu.gamebox.view.CustomProgress;
import com.shentu.gamebox.base.BaseFragment;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import static com.shentu.gamebox.utils.Constant.GAME_DETAIL;
import static com.shentu.gamebox.utils.Constant.INSTALLED;

public class GameFragment extends BaseFragment implements View.OnClickListener, Html.ImageGetter {

    private ViewPager viewpager;
    private TabLayout tablayout;
    private ArrayList<Fragment> fragments;
    private static final String tag = "gameFragment";
    private String mTag;
    private boolean needIntercept = false;
    int mLastXIntercept = 0;
    int mLastYIntercept = 0;
    /*???????????????*/
    private CustomProgress down_prograss;
    private Handler mHandler;
    private int currentProgress;
    private Button btn_download;
    /*????????????UI*/
    private JzvdStd videoView;
    private ImageView xq_game_img;
    private TextView xq_game_title;
    private TextView xq_game_rec;
    private TextView game_content;
    /*??????id*/
    private String gameId;
    private String path3;
    /*????????????bean??????*/
    private HomeItem homeItem;
    private BannerBean bannerBean;
    /*?????? ??????*/
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
    /*????????? ????????????*/
    private long downloadLength;
    private long contentLength;

    /*textview?????????????????????*/
    private int maxline = 5;
    //    private MmediaController mmediaController;
    private RelativeLayout video_view_layout;
    private ImageView play_img;
    private String url;
    /*?????????????????????*/
    private String apkname;

    private FrameLayout fragment_tab1;
    private FrameLayout fragment_tab2;
    private FrameLayout fragment_tab3;

    private boolean playWhenReady = true;
    private int currentWindow;
    private long playbackPosition;
    private ImageView exo_fullscreen_button;
    /*?????????*/
    private Constant constant;
    /*??????apk??????*/
    private File downLoadfile;
    private MyIntentReceiver receiver;
    /*???????????????*/
    private String pkName;

    private final String GAME_DOWNLOAD = "2";


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
        /*??????MainActivity ???????????????*/
        EventBus.getDefault().register(this);

        if (getArguments() != null) {
            mTag = getArguments().getString(tag);
        }
//        mHandler = new ProHandler(mActivity);
    }

    @Override
    protected void initData() {

        constant = new Constant(mActivity);
        agent_code = constant.getAgentCode();
//        Tabadd();
        requestDetail();

        LogUtils.e(constant.readUUId());
//        /*????????????????????????*/
        pkName = (String) SharePreferenceUtil.getParam(mActivity, gameId, "");
        if (constant.isAppInstalled(pkName)) {

            btn_download.setText("??????");
        }
        /*????????????*/
        /*?????????????????????*/
        String GAME_CLICK = "1";
        SendGameInfo(GAME_CLICK);
    }


    public void videoPlayer(String uri, String cover) {
        videoView.setUp(uri, "", Jzvd.SCREEN_NORMAL);
        JzvdStd.SAVE_PROGRESS = false;
        Glide.with(mActivity).load(cover).into(videoView.posterImageView);
        videoView.startVideo();
        LogUtils.e("????????????");
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

    /*MainActivity ?????????????????????*/
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


    private void SendGameInfo(String game_action) {
        HashMap<String, Object> map = FieldMapUtils.getBoxGameInfoBody( gameId, agent_code, Constant.GAME_COUNT, game_action,"");
        RetrofitManager.getInstance().GameClickCount(new Observer<HttpResult<Object>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
            }
            @Override
            public void onNext(@io.reactivex.annotations.NonNull HttpResult<Object> objectHttpResult) {
                int ret = objectHttpResult.getRet();
                String msg = objectHttpResult.getMsg();
                LogUtils.e(msg + ret);
            }
            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        },map);
    }

    /*?????????*/
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
                    /*????????????*/
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
                        videoPlayer(videoUri, cover);
                        image_view.setVisibility(View.GONE);

                    } else {
                        imgsList = JSONArray.parseArray(beanImgs, String.class);
                        image_view.setVisibility(View.VISIBLE);
                        videoView.setVisibility(View.GONE);
                        LogUtils.e(cover);
                        if (cover.isEmpty() &&null != imgsList) {
                            Glide.with(mActivity).load(imgsList.get(0)).apply(override).into(image_view);
                        } else {
                            Glide.with(mActivity).load(cover).into(image_view);
                        }
                    }
                    if (!beanRebate.equals("0")) {
                        reback_btn.setVisibility(View.VISIBLE);
                        reback_btn.setText("????????????" + rebate + "%");
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
                    /*eventbus????????????*/
                    EventBus.getDefault().postSticky(detialGameBean);
                    /*?????????tab???viewpager+fragment*/
                    VipBean vipBean = null;
                    if (null != vipBeans){
                        vipBean = vipBeans.get(0);
                    }
                    initViewPager(beanImgs, vipBean, openClose);
                }
            }
        }, map);
    }


    private void initViewPager(String beanImgs, VipBean vip, String open) {

        Fragment openareaFragment = OpenAreaFragment.newInstance("open");
        Fragment priceFragment = PriceFragment.newInstance("price");
        Fragment imageFragment = ImageFragment.newInstance("image");


        ArrayList<String> strtab = new ArrayList<>();
        fragments = new ArrayList<>();
        strtab.clear();
        if (!beanImgs.isEmpty()) {
            strtab.add("????????????");
            tablayout.addTab(tablayout.newTab().setText("????????????"));
            fragments.add(imageFragment);
        }
        if (null != vip && !vip.getLavel().isEmpty()) {
            strtab.add("VIP????????????");
            tablayout.addTab(tablayout.newTab().setText("VIP????????????"));
            fragments.add(priceFragment);
        }
        if (!open.isEmpty()) {
            strtab.add("???????????????");
            tablayout.addTab(tablayout.newTab().setText("???????????????"));
            fragments.add(openareaFragment);
        }


        FragmentTransaction fm = getChildFragmentManager().beginTransaction();
        fm.add(R.id.fragment_tab1, imageFragment);
        fm.add(R.id.fragment_tab2, priceFragment);
        fm.add(R.id.fragment_tab3, openareaFragment);
        fm.commit();
        TabLayout.Tab tabAt = tablayout.getTabAt(0);



        if (tabAt.isSelected() && tabAt.getText().equals("????????????")) {

            fragment_tab1.setVisibility(View.VISIBLE);
            fragment_tab2.setVisibility(View.GONE);
            fragment_tab3.setVisibility(View.GONE);

        }
        if (tabAt.isSelected() && tabAt.getText().equals("VIP????????????")) {
            fragment_tab1.setVisibility(View.GONE);
            fragment_tab2.setVisibility(View.VISIBLE);
            fragment_tab3.setVisibility(View.GONE);
        }
        if (tabAt.isSelected() && tabAt.getText().equals("???????????????")) {
            fragment_tab1.setVisibility(View.GONE);
            fragment_tab2.setVisibility(View.GONE);
            fragment_tab3.setVisibility(View.VISIBLE);
        }



        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("????????????")) {
                    fragment_tab1.setVisibility(View.VISIBLE);
                    fragment_tab2.setVisibility(View.GONE);
                    fragment_tab3.setVisibility(View.GONE);
                }
                if (tab.getText().equals("VIP????????????")) {
                    fragment_tab1.setVisibility(View.GONE);
                    fragment_tab2.setVisibility(View.VISIBLE);
                    fragment_tab3.setVisibility(View.GONE);
                }
                if (tab.getText().equals("???????????????")) {
                    fragment_tab1.setVisibility(View.GONE);
                    fragment_tab2.setVisibility(View.GONE);
                    fragment_tab3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getText().equals("????????????")) {
                    fragment_tab1.setVisibility(View.GONE);

//                    fm.hide(imageFragment);
                }
                if (tab.getText().equals("VIP????????????")) {
                    fragment_tab2.setVisibility(View.GONE);

//                    fm.hide(priceFragment);
                }
                if (tab.getText().equals("???????????????")) {
                    fragment_tab3.setVisibility(View.GONE);
//                    fm.hide(openareaFragment);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onClick(View v) {

        int startHeight = game_content.getLineHeight();
        switch (v.getId()) {
            //???????????? ???????????????
            case R.id.game_download:

                if ( btn_download.getText().equals("??????")) {
                    //????????????
                    LogUtils.e(isValuable(mActivity,pkName) + " ???????????????");
                    /*??????????????????????????????*/
                    if (constant.isAppInstalled(pkName) ){
                    try {
                        Intent aPackage = mActivity.getPackageManager().getLaunchIntentForPackage(pkName);
                        mActivity.startActivity(aPackage);
                    } catch (Exception e) {
                        Toast.makeText(mActivity, "????????????app", Toast.LENGTH_SHORT).show();
                    }
                    }else{
                        if (downLoadfile != null ) {
                            UpdateVersion.installApk(mActivity, downLoadfile);
                        } else {
                            btn_download.setText("??????");
                        }
                    }
                } else if (btn_download.getText().equals("??????")) {
                    /*????????????*/
                    SendGameInfo(GAME_DOWNLOAD);
                    /*????????? ????????????????????? ????????????*/
                    if (downLoadfile != null && !INSTALLED){
                        UpdateVersion.installApk(mActivity, downLoadfile);
                    }else{
                    Toast.makeText(mActivity, "?????????", Toast.LENGTH_SHORT).show();
                    down_prograss.setVisibility(View.VISIBLE);
                    btn_download.setVisibility(View.GONE);
                    requestDownUrl();
                    }
                }
                break;
            /*??????*/
            case R.id.reback_btn:
                showGiftHtml(detialGameBean.getRebate_detail());
                break;
            /*??????*/
            case R.id.gift_btn:

                showGiftHtml(detialGameBean.getGift_detail());
                break;
            case R.id.scan_more:
                int lineHeight = game_content.getLineHeight();
                int lineCount = game_content.getLineCount();
                if (scan_more.getText().equals("??????")) {

                    game_content.setHeight(lineHeight * lineCount);
                    scan_more.setText("??????");
                } else {

                    game_content.setHeight(maxline * lineHeight);
                    scan_more.setText("??????");
                }

                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        receiver = new MyIntentReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        /*????????????????????????*/
        filter.addDataScheme("package");
        mActivity.registerReceiver(receiver, filter);

    }

    class MyIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
                String packageName = intent.getDataString();

                if (packageName.contains(pkName)) {
                    INSTALLED = true;
                }
                btn_download.setText("??????");
                LogUtils.e("?????????" + packageName);
            }
            if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
                String packageName = intent.getDataString();
                if (packageName.contains(pkName)) {
                    INSTALLED = false;
                }
                btn_download.setText("??????");
                LogUtils.e("?????????" + packageName);
            }
        }
    }


    private void storeApkInfo(File file) {
        PackageManager pm = mActivity.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(file.getPath(), PackageManager.GET_ACTIVITIES);
        if (packageInfo != null) {
            String fileApkName = packageInfo.applicationInfo.packageName;
            SharePreferenceUtil.setParam(mActivity, fileApkName, gameId);
        }
    }

    private void showGiftHtml(String htmlDate) {
        if (!htmlDate.isEmpty()) {

            View view = LayoutInflater.from(mActivity).inflate(R.layout.activity_webview, null);
            DialogUtils.showHtmlDialog(mActivity, view, htmlDate);
        } else {
            Toast.makeText(mActivity, "???????????????????????????", Toast.LENGTH_SHORT).show();
        }

    }

    public String getHtmlDate(String bodyHTML) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }



    private boolean isValuable(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        /*???????????????????????????*/
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        List<String> pNames = new ArrayList<>();
        if (installedPackages != null) {
            for (PackageInfo info : installedPackages) {
                pNames.add(info.packageName);
            }
        }
        return pNames.contains(packageName);
    }

    /*????????????apk*/
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
                ApiException apiException = (ApiException) e;
                LogUtils.e(apiException.getCode() + apiException.getDispalyMessage());
//                com.jakewharton.retrofit2.adapter.rxjava2.HttpException cannot be cast to
            }

            @Override
            public void onComplete() {

                LogUtils.e(url);
                LogUtils.e(msg);
                if (null != url &&!url.isEmpty()) {
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

    /*????????????*/
    public void downFile(String url) {
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
                        //??????progressdialog ???????????????

                        down_prograss.setProgress(integer);
                        down_prograss.incrementProgressBy((int) (downloadLength * 1.0f / contentLength * 100));
                        if (contentLength == downloadLength) {
                            btn_download.setVisibility(View.VISIBLE);
                            down_prograss.setVisibility(View.GONE);
                            btn_download.setText("??????");
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Toast.makeText(mActivity, "?????????????????????????????????", Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }

    /*??????apk*/
    private void downApk(String url, ObservableEmitter<Integer> emitter) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //???????????? ????????????
                downApk(url, emitter);
//                breakPoint(url, emitter);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body() == null) {
                    //????????????
//                    breakPoint(url, emitter);
                    downApk(url, emitter);
                }
                InputStream is = null;
                FileOutputStream fos = null;
                int len;
                try {
                    is = Objects.requireNonNull(response.body()).byteStream();
//                    String[] apks = "/".split(url);
//                    LogUtils.e(apks[apks.length]);
//                    apkname = apks[apks.length];
                    downLoadfile = createFile();
                    fos = new FileOutputStream(downLoadfile);
                    byte[] bytes = new byte[2048];
                    long total = Objects.requireNonNull(response.body()).contentLength();
                    contentLength = total;
                    long sum = 0;
                    while ((len = is.read(bytes)) != -1) {
                        fos.write(bytes, 0, len);
                        sum += len;
                        int progress = (int) (len * 1.0f / total * 100);
                        /*????????? ????????????*/
                        emitter.onNext(progress);
                        downloadLength = sum;
                    }
                    fos.flush();
                    LogUtils.e("????????????");

                    /*??????apk?????? */
                    if (downLoadfile != null) {
                        storeApkInfo(downLoadfile);
                    }
                    //???????????? ??????apk
                    UpdateVersion.installApk(mActivity, downLoadfile);
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

    private File createFile() {
        String path = mActivity.getExternalFilesDir("file").getPath();
        File file = new File(path, "debug.apk");
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
        /*???????????????????????? */
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /*?????????*/
        EventBus.getDefault().unregister(this);
        /*Rxjava????????????*/
        mDisposable.dispose();
        /*????????????*/
        if (receiver != null) {
            mActivity.unregisterReceiver(receiver);
        }

        LogUtils.e(INSTALLED + "");
    }
}
