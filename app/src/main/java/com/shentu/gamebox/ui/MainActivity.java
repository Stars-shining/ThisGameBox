package com.shentu.gamebox.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shentu.gamebox.R;
import com.shentu.gamebox.adapter.GameItemAdapter;
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
import com.shentu.gamebox.utils.CrashHandlerUtils;
import com.shentu.gamebox.utils.DialogUtils;
import com.shentu.gamebox.utils.FieldMapUtils;
import com.shentu.gamebox.utils.LogUtils;
import com.shentu.gamebox.utils.Permission;
import com.shentu.gamebox.utils.SharePreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

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
    /*????????????*/
    @Keep
    private TextView hot_txt;
    @Keep
    private TextView rec_txt;
    /*??????*/
    private RecyclerView hot_recycle;
    private RecyclerView rec_recycle;

    /*??????????????????*/
    private TextView firstpg_title;
    private TextView firstpg_intro;
    private TextView firstpg_start;
    private TextView hot_more;
    private TextView rec_more;
    private ImageView firstpg_min;
    //??????&??????????????????

    private List<HomeItem> hotGameBeans;

    private List<HomeItem> recGameBeans;

    //????????????3???????????????

    private ArrayList<HomeItem> hotList;

    private List<HomeItem> recList;


    private GameItemAdapter gameAdapter;

    private List<BannerBean> bannerBeans;
    private ImageView gameBigImg;
    /*??????*/
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

    /*??????????????????*/
    private boolean isUnfold = false;
    private String uuId;
    private final boolean isFirstStart = true;


    //    private LinearLayout rec_layout_parent;


    @Override
    protected int initLayoutId() {
        return R.layout.activity_home;
    }



    @Override
    protected void initView() {

        gameBigImg = findViewById(R.id.game_big_img);
        /*????????????*/
        RelativeLayout hot_list = findViewById(R.id.hot_list);
        /*???????????????????????????*/
        include_instro = findViewById(R.id.head_item_layout);
        include_instro.findViewById(R.id.head_btn).setVisibility(View.GONE);
        /*????????????*/
        RelativeLayout rec_list = findViewById(R.id.rec_list);

        hot_recycle = hot_list.findViewById(R.id.xq_recycleview);
        hot_txt = hot_list.findViewById(R.id.firstpg_txt);
        hot_more = hot_list.findViewById(R.id.more_item);
        rec_more = rec_list.findViewById(R.id.more_item);
        rec_txt = rec_list.findViewById(R.id.firstpg_txt);
        rec_recycle = rec_list.findViewById(R.id.xq_recycleview);
        /*???????????????*/
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
//        String ss = "????????????????????????";
//        CrashHandlerUtils.getInstance().setToServer(ss);

        Constant constant = new Constant(this);
        agentCode = constant.getAgentCode();
        /*??????????????????*/
        constant.saveUniqueID();
        uuId = constant.readUUId();


        boolean first = (boolean) SharePreferenceUtil.getParam(this, "First", true);
        if (first){
            LogUtils.e("???????????????");
            SharePreferenceUtil.setParam(this,false,"First");
            /*????????????*/
            postAppInstall(Constant.INSTALL_COUNT);
        }else{
            LogUtils.e("??????????????????");
             /*????????????*/
            postAppInstall(Constant.LAUNCH_COUNT);
        }

        permission = new Permission(this);

        /*????????????*/
        gamesInfo(recType, agentCode);
        /*????????????*/
        mClick();
        /*????????????info*/
        requestBanner(agentCode);

        /*????????????*/
        CheckVersionCode(agentCode);

    }



    private void postAppInstall(String action) {

        /*params?????? 1???agent_code  2???uuid */
        HashMap<String, Object> map = FieldMapUtils.getBoxGameInfoBody("", agentCode, action, "", uuId);

        RetrofitManager.getInstance().BoxinstallCount(new Observer<HttpResult<Object>>() {
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
        }, map);

    }

    private void gamesInfo(String type, String agent_code) {

        hotList = new ArrayList<>();
        recList = new ArrayList<>();

        HashMap<String, Object> map = FieldMapUtils.getRequestBody(type, "", agent_code, Constant.GET_GAMES, "");
        if (type.equals("2")) {
            //????????????
            requestRecGames(map);

        } else {
            //????????????
            requestHotGames(map);
        }

    }

    /*??????????????????games??????*/

    public void requestHotGames(HashMap<String, Object> map) {
//        RequestBody requestBody = getRequestBody();
        RetrofitManager.getInstance().GameListInfo(new Observer<HttpResult<GameBean<HomeItem>>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull HttpResult<GameBean<HomeItem>> gameBeanHttpResult) {
                //??????game??????
                GameBean<HomeItem> data = gameBeanHttpResult.getData();
                hotGameBeans = data.getList();
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                ApiException apiException = (ApiException) e;
                LogUtils.e(apiException.getCode() + apiException.getDispalyMessage());
            }

            @Override
            public void onComplete() {
                /*??????????????????3???*/
                if (hotGameBeans != null && hotGameBeans.size() != 0) {
                    hot_txt.setText("????????????");
                    if (hotGameBeans.size() > 3) {
                        if (isUnfold) {
                            hot_more.setVisibility(View.VISIBLE);
                            hotList.addAll(hotGameBeans.subList(0, 3));
                            /*??????recyclerview*/
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
                //??????game??????
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

                ApiException apiException = (ApiException) e;
                LogUtils.e(apiException.getCode() + apiException.getDispalyMessage());
            }

            @Override
            public void onComplete() {
                /*??????????????????3???*/
                if (recGameBeans != null && recGameBeans.size() != 0) {
                    rec_txt.setText("????????????");
                    if (recGameBeans.size() > 3 ) {
                        recList.addAll(recGameBeans.subList(0, 3));
                        /*??????recyclerview*/
                        setRecyclerView(rec_recycle, recList, RECTYPE);
                        rec_more.setVisibility(View.VISIBLE);
                    } else {
//                        rec_more.setVisibility(View.GONE);
                        setRecyclerView(rec_recycle, recGameBeans, RECTYPE);
                    }
                }

                /*????????????*/
                gamesInfo(hotType, agentCode);
            }
        }, map);


    }

    /*????????????*/
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
                ApiException apiException = (ApiException) e;
                LogUtils.e(apiException.getCode() + apiException.getDispalyMessage());
            }

            @Override
            public void onComplete() {
                if (bannerBeans != null) {
                    //??????????????????
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

    /*??????*/
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
//                phone_num.setText("???????????????" + assistantBean.getKf_number());
                ViewTreeObserver vo = phone_num.getViewTreeObserver();
                vo.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        TextPaint paint = phone_num.getPaint();
                        paint.setTextSize(phone_num.getTextSize());
                        int v = (int) paint.measureText("???????????????" + assistantBean.getKf_number());
                        if (v> phone_num.getWidth()){
                            phone_num.setText("???????????????" +"\n"+ assistantBean.getKf_number());
                        }
                    }
                });
                work_time.setText("???????????????" + assistantBean.getKf_time());
                DialogUtils.getDialog(MainActivity.this, dialogView);

            }
        }, map);
    }


    public void mClick() {
        rec_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recGameBeans != null&& recGameBeans.size() >3){

                    List<HomeItem> homeItems = recGameBeans.subList(0, 3);
                    if (rec_more.getText().equals("??????")) {
                        setRecyclerView(rec_recycle, homeItems, RECTYPE);
//                    gameAdapter.notifyDataSetChanged();
                        rec_more.setText("????????????");
                    } else {
                        setRecyclerView(rec_recycle, recGameBeans, RECTYPE);
//                    gameAdapter.notifyDataSetChanged();
                        rec_more.setText("??????");
                    }
                }
            }
        });
        hot_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hotGameBeans != null && hotGameBeans.size() >3){

                List<HomeItem> homeItems = hotGameBeans.subList(0, 3);
                if (hot_more.getText().equals("??????")) {
                    setRecyclerView(hot_recycle, homeItems, HOTTYPE);
//                    gameAdapter.notifyDataSetChanged();
                    hot_more.setText("????????????");
                } else {
                    setRecyclerView(hot_recycle, hotGameBeans, HOTTYPE);
//                    gameAdapter.notifyDataSetChanged();
                    hot_more.setText("??????");
                }
                }
            }
        });

    }

    /*????????????*/
    public void CheckVersionCode(String agent_code) {
        /*agent_code  version parent_version*/
        UpdateVersion version;

//        version = new UpdateVersion(MainActivity.this);
        /*????????????*/
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
                /*????????????*/
                if (ret == 100) {
                    /*???????????? ?????????*/
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_update, null);
                    Button updateBtn = view.findViewById(R.id.update_apk);
                    Button cancel_btn = view.findViewById(R.id.cancel_btn);
//                    TextView dateVersion = view.findViewById(R.id.update_version);
                    TextView versionContent = view.findViewById(R.id.update_content);

//                    dateVersion.setText("???????????????" );
                    versionContent.setText(msg);

                    builder.setView(view);
                    builder.setCancelable(false);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                    updateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            permission.initPermission();

                            /*????????????*/
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

    /*????????????*/
    public void downFile(String url) {

        ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("????????????");
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
                        //??????progressdialog ???????????????
                        int t = (int) (downloadLength * 1.0f / contentLength * 100);
                        dialog.setProgress(t);
                        if (downloadLength == contentLength) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Toast.makeText(MainActivity.this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MainActivity.this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    /*??????apk*/
    private void downApk(String url, ObservableEmitter<Integer> emitter) {
        UpdateVersion updateVersion = new UpdateVersion(this);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {

            private String apkname;

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //???????????? ????????????
//                downApk(url, emitter);
                updateVersion.breakPoint(url, emitter);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body() == null) {
                    //????????????
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

                        /*????????? ????????????*/
                        emitter.onNext(progress);
                        downloadLength = sum;
                    }
                    fos.flush();
                    LogUtils.e("????????????");
                    //???????????? ??????apk
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

        //setLayoutManager??????????????? ????????????????????????
        view.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        view.setHasFixedSize(true);
        view.setAdapter(gameAdapter);
        gameAdapter.addChildClickViewIds(R.id.firstpg_detial_btn);
        gameAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                HomeItem homeItem = list.get(position);
                startGameActivity(type,homeItem);
            }
        });
        gameAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                HomeItem homeItem = list.get(position);
                startGameActivity(type,homeItem);
                LogUtils.e("?????????");
            }
        });

        /*?????????????????????*/
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


    /*???????????????*/
    private void startGameActivity(int type, HomeItem homeItem) {
        Bundle bundle = new Bundle();
        LogUtils.e(type + "");
        bundle.putInt("type", type);
        bundle.putSerializable("homeItem", homeItem);
        EventBus.getDefault().postSticky(bundle);
        Intent intent = new Intent(this, GameDetialActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.assistant:
                /*???????????????*/
                Constant constant = new Constant(MainActivity.this);
                String code = constant.getAgentCode();
                requestAssistant(code);

                /*??????????????? ????????????????????????*/
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
