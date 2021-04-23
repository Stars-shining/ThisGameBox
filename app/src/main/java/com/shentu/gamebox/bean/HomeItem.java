package com.shentu.gamebox.bean;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class HomeItem implements Serializable {



    private String id;
    private String name;
    private String icon;
    private String intro;
    private String open_time;
    private String cover;
    private String gift_detail;
    private String rebate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getGift_detail() {
        return gift_detail;
    }

    public void setGift_detail(String gift_detail) {
        this.gift_detail = gift_detail;
    }

    public String getRebate() {
        return rebate;
    }

    public void setRebate(String rebate) {
        this.rebate = rebate;
    }

    @Override
    public String toString() {
        return "HomeItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", intro='" + intro + '\'' +
                ", open_time='" + open_time + '\'' +
                ", cover='" + cover + '\'' +
                ", gift_detail='" + gift_detail + '\'' +
                ", rebate='" + rebate + '\'' +
                '}';
    }
}
