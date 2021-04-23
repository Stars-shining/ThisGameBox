package com.shentu.gamebox.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class ParamBean {
    /*
     * fastJson 序列化对象
     * 根据指定顺序
     * */
    @JSONField(ordinal = 1)
    private String type;

    @JSONField(ordinal = 2)
    private String agent_code;

    @JSONField(ordinal = 4)
    private String username;

    @JSONField(ordinal = 3)
    private String game_id;

    @JSONField(ordinal = 5)
    private String agent_version;


    public String getAgent_version() {
        return agent_version;
    }

    public void setAgent_version(String agent_version) {
        this.agent_version = agent_version;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAgent_code() {
        return agent_code;
    }

    public void setAgent_code(String agent_code) {
        this.agent_code = agent_code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ParamBean{" +
                "type='" + type + '\'' +
                ", agent_code='" + agent_code + '\'' +
                ", username='" + username + '\'' +
                ", game_id='" + game_id + '\'' +
                '}';
    }
}
