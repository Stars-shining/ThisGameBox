package com.shentu.gamebox.bean;

public class VersionBean {
    /*
    rec =100 msg = 最新版本
    "agent_code": "5fae2f",
        "version": 0,
        "parent_version": 0,
        "force": 0,
        "url": ""*/
    /*"agent_code": "5fae2f",
        "force": "0",
        "url": "https://apk.storage.gslbcache.cn/appgo/4918/4918_14152_27863_27864_sign.apk"*/
    private  String agent_code;
    private  String force;
    private  String url;

    public String getAgent_code() {
        return agent_code;
    }

    public void setAgent_code(String agent_code) {
        this.agent_code = agent_code;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
