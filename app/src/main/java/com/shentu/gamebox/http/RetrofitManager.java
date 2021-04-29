package com.shentu.gamebox.http;

import androidx.annotation.Keep;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.shentu.gamebox.BuildConfig;
import com.shentu.gamebox.bean.AssistantBean;
import com.shentu.gamebox.bean.BannerBean;
import com.shentu.gamebox.bean.DetailBean;

import com.shentu.gamebox.bean.DownLoadBean;
import com.shentu.gamebox.bean.GameBean;
import com.shentu.gamebox.bean.HomeItem;
import com.shentu.gamebox.bean.HttpResult;

import com.shentu.gamebox.bean.VersionBean;
import com.shentu.gamebox.utils.Constant;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Keep
public class RetrofitManager {

    private static Retrofit retrofit;
    private static RetrofitManager instance;

    private static ApiService apiService;


    //获取单例
    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }


    private static OkHttpClient getOkHttpClient() {
        //定制OkHttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            //新建log拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            //日志显示级别
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            /*链接超时*/
            builder.connectTimeout(15, TimeUnit.SECONDS);
            /*数据读取*/
            builder.readTimeout(15, TimeUnit.SECONDS);
            /*数据写入*/
            builder.writeTimeout(15, TimeUnit.SECONDS);
            //OkHttp进行添加拦截器loggingInterceptor
            builder.addInterceptor(loggingInterceptor);

        }
        return builder.build();
    }


    public static ApiService getApiService() {
        if (null == apiService) {
            synchronized (ApiService.class) {
                if (null == apiService) {
                    apiService = retrofit.create(ApiService.class);
                }
            }
        }
        return apiService;
    }

    public void init() {

        retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(Constant.normalUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }


    /*热门游戏列表*/
    public void GameListInfo(Observer<HttpResult<GameBean<HomeItem>>> observer, HashMap<String, Object> map) {
        getApiService().getGameList(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

//    /*推荐游戏列表*/
//    public void RecGameListInfo(Observer<HttpResult<GameBean<RecGameBean>>> observer, HashMap<String, Object> map) {
//        getApiService().getRecGameList(map).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//
//    }

    /*游戏详情*/
    public void GameDetailInfo(Observer<HttpResult<DetailBean>> observer, HashMap<String, Object> map) {
        getApiService().getGameDetail(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /*游戏客服*/
    public void AssistantInfo(Observer<HttpResult<AssistantBean>> observer, HashMap<String, Object> map) {
        getApiService().getAssistant(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /*横幅游戏*/
    public void BannerInfo(Observer<HttpResult<GameBean<BannerBean>>> observer, HashMap<String, Object> map) {
        getApiService().getBanner(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /*apk游戏*/
    public void DownLoadGame(Observer<HttpResult<DownLoadBean>> observer, HashMap<String, Object> map) {
        getApiService().download(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /*apk游戏*/
    public void CheckVersion(Observer<HttpResult<VersionBean>> observer, HashMap<String, Object> map) {
        getApiService().checkVersion(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /*游戏计数  map  params参数 1、agent_code  2、game_id 3、game_action*/
    public void GameClickCount(Observer<HttpResult<Object>> observer, HashMap<String, Object> map) {
        getApiService().gameClickCount(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

//    /*启动计数  map  params参数 1、agent_code  2、uuid*/
//    public void BoxStartCount(Observer<HttpResult<Object>> observer, HashMap<String, Object> map) {
//        getApiService().boxStartCount(map).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//    }

    /*安装计数   map  params参数 1、agent_code  2、uuid */
    public void BoxinstallCount(Observer<HttpResult<Object>> observer, HashMap<String, Object> map) {
        getApiService().boxInstallCount(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


}
