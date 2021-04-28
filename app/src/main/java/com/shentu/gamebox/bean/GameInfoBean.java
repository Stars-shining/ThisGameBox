package com.shentu.gamebox.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class GameInfoBean {
    /*
     * fastJson 序列化对象
     * 根据指定顺序
     * */
//    @JSONField(ordinal = 1)
//    private String type;

    @JSONField(ordinal = 1)
    private String agent_code;

    @JSONField(ordinal = 3)
    private String imei;

    @JSONField(ordinal = 2)
    private String game_id;

    @JSONField(ordinal = 4)
    private String game_action;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }

    public String getAgent_code() {
        return agent_code;
    }

    public void setAgent_code(String agent_code) {
        this.agent_code = agent_code;
    }

    public String getGame_action() {
        return game_action;
    }

    public void setGame_action(String game_action) {
        this.game_action = game_action;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }
}
