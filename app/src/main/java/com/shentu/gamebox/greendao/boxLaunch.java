package com.shentu.gamebox.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class boxLaunch {
    @Id(autoincrement = true)
    private long id;
    private String agent_code;
    private String imei;
    private int count;
    private String date_str;
    private int date_int;
    @Generated(hash = 1554669143)
    public boxLaunch(long id, String agent_code, String imei, int count,
            String date_str, int date_int) {
        this.id = id;
        this.agent_code = agent_code;
        this.imei = imei;
        this.count = count;
        this.date_str = date_str;
        this.date_int = date_int;
    }
    @Generated(hash = 548354785)
    public boxLaunch() {
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
    public int getCount() {
        return this.count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String getDate_str() {
        return this.date_str;
    }
    public void setDate_str(String date_str) {
        this.date_str = date_str;
    }
    public int getDate_int() {
        return this.date_int;
    }
    public void setDate_int(int date_int) {
        this.date_int = date_int;
    }
    public void setId(long id) {
        this.id = id;
    }
    
}
