package com.shentu.gamebox.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class boxInstall {
    @Id(autoincrement = true)
    private  long id;
    private String agent_code;
    private String imei;
    private int add_time;

    public boxInstall(int id, String agent_code, String imei, int add_time) {
        this.id = id;
        this.agent_code = agent_code;
        this.imei = imei;
        this.add_time = add_time;
    }
    @Generated(hash = 1739239213)
    public boxInstall() {
    }
    @Generated(hash = 1911116576)
    public boxInstall(long id, String agent_code, String imei, int add_time) {
        this.id = id;
        this.agent_code = agent_code;
        this.imei = imei;
        this.add_time = add_time;
    }
    public long getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getAgent_code() {
        return this.agent_code;
    }
    public void setAgent_code(String agent_code) {
        this.agent_code = agent_code;
    }
    public String getImei() {
        return this.imei;
    }
    public void setImei(String imei) {
        this.imei = imei;
    }
    public int getAdd_time() {
        return this.add_time;
    }
    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }
    public void setId(long id) {
        this.id = id;
    }


}
