package com.shentu.gamebox.ui;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shentu.gamebox.R;
import com.shentu.gamebox.adapter.GameItemAdapter;
import com.shentu.gamebox.base.BaseApplication;
import com.shentu.gamebox.bean.AssistantBean;
import com.shentu.gamebox.bean.BannerBean;
import com.shentu.gamebox.bean.GameBean;
import com.shentu.gamebox.bean.HomeItem;
import com.shentu.gamebox.bean.HttpResult;

import com.shentu.gamebox.bean.VersionBean;

import com.shentu.gamebox.http.ApiException;
import com.shentu.gamebox.http.RetrofitManager;
import com.shentu.gamebox.base.BaseActivity;
import com.shentu.gamebox.http.UpdateVersion;
import com.shentu.gamebox.utils.Constant;
import com.shentu.gamebox.utils.DialogUtils;
import com.shentu.gamebox.utils.FieldMapUtils;
import com.shentu.gamebox.utils.LogUtils;
import com.shentu.gamebox.utils.Permission;
import com.shentu.gamebox.utils.SharePreferenceUtil;
import com.shentu.gamebox.utils.usage.PackageInfo;
import com.shentu.gamebox.utils.usage.UseTimeDateManager;
import com.shentu.gamebox.view.CustomProgress;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView gameLayout;
    private ArrayList<Fragment> fragments;
    /*列表名称*/

    private TextView hot_txt;

    private TextView rec_txt;
    /*列表*/
    private RecyclerView hot_recycle;
    private RecyclerView rec_recycle;
    /*大图详情btn*/
    private Button firstp_detial_btn;

    /*游戏文字介绍*/
    private TextView firstpg_title;
    private TextView firstpg_intro;
    private TextView firstpg_start;
    private TextView hot_more;
    private TextView rec_more;
    private ImageView firstpg_min;
    //热门&推荐游戏列表

    private List<HomeItem> hotGameBeans;

    private List<HomeItem> recGameBeans;

    //首次存放3条数据集合

    private ArrayList<HomeItem> hotList;

    private List<HomeItem> recList;


    private GameItemAdapter gameAdapter;

    private List<BannerBean> bannerBeans;
    private ImageView gameBigImg;
    /*客服*/
    private ImageView assistant;
    private ProgressBar update_progres;
    private View dialogView;

    private Permission permission;
    private ConstraintLayout include_instro;

    private int HOTTYPE = 1;

    private int RECTYPE = 2;

    private String hotType = "1";

    private String recType = "2";


    private String agentCode;
    private Disposable mDisposable;
    private ImageView rec_head_img;
    private TextView rec_head_title;
    private TextView rec_head_intro;
    private ImageView rec_head_bigimg;
    private ConstraintLayout rec_layout;
    private long contentLength;
    private long downloadLength;
    private String agentVersion;

    /*是否全部展开*/
    private boolean isUnfold = false;
    private UseTimeDateManager useManager;
    //    private LinearLayout rec_layout_parent;


    @Override
    protected int initLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void setTitle() {
        //隐藏actionbanr
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();

    }

    @Override
    protected void initView() {

        gameBigImg = findViewById(R.id.game_big_img);
        /*热门版本*/
        RelativeLayout hot_list = findViewById(R.id.hot_list);
        /*首页头部游戏小布局*/
        include_instro = findViewById(R.id.head_item_layout);
        include_instro.findViewById(R.id.head_btn).setVisibility(View.GONE);
        /*推荐游戏*/
        RelativeLayout rec_list = findViewById(R.id.rec_list);

        hot_recycle = hot_list.findViewById(R.id.xq_recycleview);
        hot_txt = hot_list.findViewById(R.id.firstpg_txt);
        hot_more = hot_list.findViewById(R.id.more_item);
        rec_more = rec_list.findViewById(R.id.more_item);
        rec_txt = rec_list.findViewById(R.id.firstpg_txt);
        rec_recycle = rec_list.findViewById(R.id.xq_recycleview);
        /*头部子布局*/
        firstp_detial_btn = findViewById(R.id.firstpg_detial_btn);
        firstpg_title = findViewById(R.id.head_title);
        firstpg_intro = findViewById(R.id.head_intro);
//        firstpg_start = findViewById(R.id.firstpg_open);
        firstpg_min = findViewById(R.id.head_img);

        assistant = findViewById(R.id.assistant);

        hot_more.setOnClickListener(this);
        rec_more.setOnClickListener(this);
        assistant.setOnClickListener(this);
        include_instro.setOnClickListener(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initData() {

        Constant constant = new Constant(this);
        agentCode = constant.getAgentCode();

        /*保存设备标识*/
        constant.saveUniqueID();
        /*保存首次开启时间*/
        String openTime = (String) SharePreferenceUtil.getParam(this, "openTime", "");
        if (null != openTime && !openTime.isEmpty()) {
            SharePreferenceUtil.setParam(this, Constant.getCurrentTime(), "openTime");
        }
        /*游戏列表*/
        gamesInfo(recType, agentCode);
        /*查看更多*/
        mClick();
        /*横幅游戏info*/
        requestBanner(agentCode);

        /*版本检测*/
        CheckVersionCode(agentCode);

        /*usetimeManager 初始化*/
        useManager = UseTimeDateManager.getInstance(this);
        useManager.refreshData(0);
        /*app 打开次数 使用时间*/
        getAppUseInfo();

    }

    private void getAppUseInfo() {
        ArrayList<PackageInfo> packageInfos = useManager.getmPackageInfoListOrderByTime();
        for (PackageInfo info: packageInfos) {
            LogUtils.e(JSON.toJSON(info)+"");
        }
    }

    private void gamesInfo(String type, String agent_code) {

        hotList = new ArrayList<>();
        recList = new ArrayList<>();

        HashMap<String, Object> map = FieldMapUtils.getRequestBody(type, "", agent_code, Constant.GET_GAMES, "");
        if (type.equals("2")) {
            //推荐游戏
            requestRecGames(map);
        } else {
            //热门版本
            requestHotGames(map);
        }
    }

    /*请求网络获取games数据*/

    public void requestHotGames(HashMap<String, Object> map) {
//        RequestBody requestBody = getRequestBody();
        RetrofitManager.getInstance().GameListInfo(new Observer<HttpResult<GameBean<HomeItem>>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull HttpResult<GameBean<HomeItem>> gameBeanHttpResult) {
                //获取game数据
                GameBean<HomeItem> data = gameBeanHttpResult.getData();
                hotGameBeans = data.getList();
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//                LogUtils.e(e.toString());
//                ApiException apiException = (ApiException) e;
//                LogUtils.e(apiException.getCode() + apiException.getDispalyMessage());
            }

            @Override
            public void onComplete() {
                /*首次加载显示3条*/
                if (hotGameBeans != null && hotGameBeans.size() != 0) {
                    hot_txt.setText("热门版本");
                    if (hotGameBeans.size() > 3) {
                        if (isUnfold) {
                            hot_more.setVisibility(View.VISIBLE);
                            hotList.addAll(hotGameBeans.subList(0, 3));
                            /*填充recyclerview*/
                            setRecyclerView(hot_recycle, hotList, HOTTYPE);
                        } else {
                            setRecyclerView(hot_recycle, hotGameBeans, HOTTYPE);
                        }
                    } else {
//                        hot_more.setVisibility(View.GONE);
                        setRecyclerView(hot_recycle, hotGameBeans, HOTTYPE);
                    }
                }


            }
        }, map);


    }

    public void requestRecGames(HashMap<String, Object> map) {
//        RequestBody requestBody = getRequestBody();
        RetrofitManager.getInstance().GameListInfo(new Observer<HttpResult<GameBean<HomeItem>>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull HttpResult<GameBean<HomeItem>> gameBeanHttpResult) {
                //获取game数据
                GameBean<HomeItem> data = gameBeanHttpResult.getData();
                recGameBeans = data.getList();
                if (null != recGameBeans && recGameBeans.size() != 0) {
                    isUnfold = true;
                } else {
                    isUnfold = false;
                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//                LogUtils.e(e.toString());
//                ApiException apiException = (ApiException) e;
//                LogUtils.e(apiException.getCode() + apiException.getDispalyMessage());
            }

            @Override
            public void onComplete() {
                /*首次加载显示3条*/
                if (recGameBeans != null && recGameBeans.size() != 0) {
                    rec_txt.setText("推荐游戏");
                    if (recGameBeans.size() > 3 && hotGameBeans.size() != 0) {
                        recList.addAll(recGameBeans.subList(0, 3));
                        /*填充recyclerview*/
                        setRecyclerView(rec_recycle, recList, RECTYPE);
                        rec_more.setVisibility(View.VISIBLE);
                    } else {
                        rec_more.setVisibility(View.GONE);
                        setRecyclerView(rec_recycle, recGameBeans, RECTYPE);
                    }
                }

                /*热门游戏*/
                gamesInfo(hotType, agentCode);
            }
        }, map);


    }

    /*横幅图片*/
    public void requestBanner(String agent_code) {
        HashMap<String, Object> map = FieldMapUtils.getRequestBody("", "", agent_code, Constant.GET_BANNER, "");
        RetrofitManager.getInstance().BannerInfo(new Observer<HttpResult<GameBean<BannerBean>>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull HttpResult<GameBean<BannerBean>> gameBeanHttpResult) {
                bannerBeans = gameBeanHttpResult.getData().getList();
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//                ApiException apiException = (ApiException) e;
//                LogUtils.e(apiException.getCode() + apiException.getDispalyMessage());
            }

            @Override
            public void onComplete() {
                if (bannerBeans != null) {
                    //设置图片圆角
                    RoundedCorners roundedCorners = new RoundedCorners(10);
                    RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);

                    Glide.with(getApplication()).load(bannerBeans.get(0).getGame_icon()).apply(options).into(firstpg_min);
                    Glide.with(getApplication()).load(bannerBeans.get(0).getImg()).apply(options).into(gameBigImg);

                    firstpg_title.setText(bannerBeans.get(0).getGame_name());
                    int color = ContextCompat.getColor(MainActivity.this, R.color.white);
                    firstpg_title.setTextColor(color);
                    firstpg_intro.setText(bannerBeans.get(0).getGame_intro());
                    firstpg_intro.setTextColor(color);
                    include_instro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, GameDetialActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("bannerBean", bannerBeans.get(0));
                            intent.putExtras(bundle);
                            startActivity(intent);
                            EventBus.getDefault().postSticky(bundle);
                        }
                    });
                }
            }
        }, map);


    }

    /*客服*/
    public void requestAssistant(String agent_code) {
        HashMap<String, Object> map = FieldMapUtils.getRequestBody("", "", agent_code, Constant.GET_CONTACT, "");
        RetrofitManager.getInstance().AssistantInfo(new Observer<HttpResult<AssistantBean>>() {

            private AssistantBean assistantBean;

            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                mDisposable = d;

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull HttpResult<AssistantBean> gameBeanHttpResult) {
                assistantBean = gameBeanHttpResult.getData();
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                ApiException apiException = (ApiException) e;
                LogUtils.e(apiException.getCode() + apiException.getDispalyMessage());
            }

            @Override
            public void onComplete() {
                dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_layout, null);
                TextView phone_num = dialogView.findViewById(R.id.phone_num);
                TextView work_time = dialogView.findViewById(R.id.work_time);
                phone_num.setText(String.format("联系客服：%s", assistantBean.getKf_number()));
                work_time.setText(String.format("工作时间：%s", assistantBean.getKf_time()));
                DialogUtils.getDialog(MainActivity.this, dialogView);

            }
        }, map);
    }


    public void mClick() {
        rec_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerView(rec_recycle, recGameBeans, RECTYPE);
                gameAdapter.notifyDataSetChanged();
                v.setVisibility(View.GONE);


            }
        });
        hot_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<HomeItem> homeItems = hotGameBeans.subList(0, 3);
                if (hot_more.getText().equals("收起")) {
                    setRecyclerView(hot_recycle, homeItems, HOTTYPE);
                    gameAdapter.notifyDataSetChanged();
                    hot_more.setText("查看更多");
                } else {
                    setRecyclerView(hot_recycle, hotGameBeans, HOTTYPE);
                    gameAdapter.notifyDataSetChanged();
                    hot_more.setText("收起");
                }


            }
        });

    }

    /*检查版本*/
    public void CheckVersionCode(String agent_code) {
        /*agent_code  version parent_version*/
        UpdateVersion version;

//        version = new UpdateVersion(MainActivity.this);
        /*本地版本*/
//        int versionCode = version.getVersionCode();

        String agent_version = getResources().getString(R.string.agent_version);

        HashMap<String, Object> map = FieldMapUtils.getRequestBody("", "", agent_code, Constant.CHECK_VERSION, "", agent_version);

        RetrofitManager.getInstance().CheckVersion(new Observer<HttpResult<VersionBean>>() {

            private String url;
            private int updateCode;
            private String msg;
            private VersionBean versionBean;
            private int ret;

            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull HttpResult<VersionBean> gameBeanHttpResult) {
                msg = gameBeanHttpResult.getMsg();
                ret = gameBeanHttpResult.getRet();
                versionBean = gameBeanHttpResult.getData();
//                updateCode = versionBean.getVersion();
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

                url = versionBean.getUrl();

                if (ret == 101)
                    LogUtils.e(msg);
                /*最新版本*/
                if (ret == 100) {
                    /*最新版本 不更新*/
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_update, null);
                    Button updateBtn = view.findViewById(R.id.update_apk);
                    Button cancel_btn = view.findViewById(R.id.cancel_btn);
//                    TextView dateVersion = view.findViewById(R.id.update_version);
                    TextView versionContent = view.findViewById(R.id.update_content);

//                    dateVersion.setText("最新版本：" );
                    versionContent.setText(msg);

                    builder.setView(view);
                    builder.setCancelable(false);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                    updateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Permission.initPermission();

                            /*下载文件*/
                            if (!url.isEmpty()) {
                                alertDialog.dismiss();
                                downFile(url);
                            } else {
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                                CheckVersionCode(agent_code);
                            }
                        }
                    });
                    if (versionBean.getForce().equals("0")) {
                        cancel_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                    } else {
                        cancel_btn.setVisibility(View.GONE);
                    }
                }
            }
        }, map);

    }

    /*下载文件*/
    public void downFile(String url) {

        ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("正在更新");
        dialog.setMax(100);
        dialog.setCancelable(false);
        dialog.show();

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
                        int t = (int) (downloadLength * 1.0f / contentLength * 100);
                        dialog.setProgress(t);
                        if (downloadLength == contentLength) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Toast.makeText(MainActivity.this, "网络异常，请重新下载！", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MainActivity.this, "服务器异常，请重新下载！", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    /*下载apk*/
    private void downApk(String url, ObservableEmitter<Integer> emitter) {
        UpdateVersion updateVersion = new UpdateVersion(this);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {

            private String apkname;

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //下载失败 断点续传
//                downApk(url, emitter);
                updateVersion.breakPoint(url, emitter);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body() == null) {
                    //下载失败
                    updateVersion.breakPoint(url, emitter);
//                    downApk(url, emitter);
                }

                InputStream is = null;
                FileOutputStream fos = null;
                int len;
                try {
                    is = response.body().byteStream();
                    String[] apks = "/".split(url);
                    apkname = apks[apks.length - 1];
                    File file = createFile(apkname);
                    fos = new FileOutputStream(file);
                    byte[] bytes = new byte[1024];
                    long total = response.body().contentLength();
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
                    //下载完成 安装apk
                    UpdateVersion.installApk(MainActivity.this, file);
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
        String path = MainActivity.this.getExternalFilesDir("dir").getAbsolutePath();
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


    private void setRecyclerView(RecyclerView view, List<HomeItem> list, int type) {

        gameAdapter = new GameItemAdapter(R.layout.shouye_item, list, type);

        //setLayoutManager布局管理器 有多种布局可选择
        view.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        view.setHasFixedSize(true);
        view.setAdapter(gameAdapter);
        gameAdapter.addChildClickViewIds(R.id.firstpg_detial_btn);
        gameAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                HomeItem homeItem = list.get(position);
                Bundle bundle = new Bundle();
                LogUtils.e(type + "");
                bundle.putInt("type", type);
                bundle.putSerializable("homeItem", homeItem);
                EventBus.getDefault().postSticky(bundle);
                startGameActivity();
            }
        });
        gameAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

                HomeItem homeItem = list.get(position);
                Bundle bundle = new Bundle();
                LogUtils.e(type + "");
                bundle.putInt("type", type);
                bundle.putSerializable("homeItem", homeItem);
                EventBus.getDefault().postSticky(bundle);

                if (view.getId() == R.id.firstpg_detial_btn) {
                    startGameActivity();
                }
            }
        });
        /*滑动不加载图片*/
        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(getApplication()).resumeRequests();
                } else {
                    Glide.with(getApplication()).pauseRequests();
                }
            }
        });
    }


    /*跳转详情页*/
    private void startGameActivity() {
        Intent intent = new Intent(this, GameDetialActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.assistant:
                /*弹出框*/
                Constant constant = new Constant(MainActivity.this);
                String code = constant.getAgentCode();
                requestAssistant(code);

                /*实现可滑动 避免遮挡详情按钮*/
//                slideView();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }
}
