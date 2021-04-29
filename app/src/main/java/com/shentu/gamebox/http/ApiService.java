package com.shentu.gamebox.http;

import androidx.annotation.Keep;

import com.shentu.gamebox.bean.AssistantBean;
import com.shentu.gamebox.bean.BannerBean;
import com.shentu.gamebox.bean.DetailBean;

import com.shentu.gamebox.bean.DownLoadBean;
import com.shentu.gamebox.bean.GameBean;
import com.shentu.gamebox.bean.HomeItem;
import com.shentu.gamebox.bean.HttpResult;

import com.shentu.gamebox.bean.VersionBean;

import java.util.Map;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

@Keep
public interface ApiService {

    /*@Path  @Query @QueryMap @Field  @Body */

    @FormUrlEncoded
    @POST("/game_box")
    Observable<HttpResult<DetailBean>> getGameDetail(@FieldMap Map<String, Object> map);

    /*游戏列表  type ：1 专区游戏 2 推荐游戏*/
    @FormUrlEncoded
    @POST("/game_box")
    Observable<HttpResult<GameBean<HomeItem>>> getGameList(@FieldMap Map<String, Object> map);

//    /*游戏列表  type ：1 专区游戏 2 推荐游戏*/
//    @FormUrlEncoded
//    @POST("/game_box")
//    Observable<HttpResult<GameBean<RecGameBean>>> getRecGameList(@FieldMap Map<String, Object> map);
//
    /*客服*/
    @FormUrlEncoded
    @POST("/game_box")
    Observable<HttpResult<AssistantBean>> getAssistant(@FieldMap Map<String, Object> map);

    /*横幅*/
    @FormUrlEncoded
    @POST("/game_box")
    Observable<HttpResult<GameBean<BannerBean>>> getBanner(@FieldMap Map<String, Object> map);
    /*下载*/
    @FormUrlEncoded
    @POST("/game_box/down_url_a")
    Observable<HttpResult<DownLoadBean>> download(@FieldMap Map<String, Object> map);
    /*版本检测*/
    @FormUrlEncoded
    @POST("/game_box")
    Observable<HttpResult<VersionBean>> checkVersion(@FieldMap Map<String, Object> map);
    /*安装*/
    @FormUrlEncoded
    @POST("/game_box")
    Observable<HttpResult<Object>> boxInstallCount(@FieldMap Map<String, Object> map);

//    @FormUrlEncoded
//    @POST("/game_box")
//    Observable<HttpResult<Object>> boxStartCount(@FieldMap Map<String, Object> map);
    /*点击*/
    @FormUrlEncoded
    @POST("/game_box")
    Observable<HttpResult<Object>> gameClickCount(@FieldMap Map<String, Object> map);


}
