package com.shentu.gamebox.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {@Index(value = "agent_code DESC,date_int DESC",unique = true)})
public class boxData {
    @Id(autoincrement = true)
    private long id;
    private String agent_code;
    private  int agent_id;
    private int sub_agent_id;
    private int shangwu_id;
    private int new_device;
    private int launch;
    private int zq_click;
    private int zq_download;
    private int tj_click;
    private int tj_download;
    private String date_str;
    private int date_int;
    @Generated(hash = 630219905)
    public boxData(long id, String agent_code, int agent_id, int sub_agent_id,
            int shangwu_id, int new_device, int launch, int zq_click,
            int zq_download, int tj_click, int tj_download, String date_str,
            int date_int) {
        this.id = id;
        this.agent_code = agent_code;
        this.agent_id = agent_id;
        this.sub_agent_id = sub_agent_id;
        this.shangwu_id = shangwu_id;
        this.new_device = new_device;
        this.launch = launch;
        this.zq_click = zq_click;
        this.zq_download = zq_download;
        this.tj_click = tj_click;
        this.tj_download = tj_download;
        this.date_str = date_str;
        this.date_int = date_int;
    }
    @Generated(hash = 1615993580)
    public boxData() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getAgent_code() {
        return this.agent_code;
    }
    public void setAgent_code(String agent_code) {
        this.agent_code = agent_code;
    }
    public int getAgent_id() {
        return this.agent_id;
    }
    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }
    public int getSub_agent_id() {
        return this.sub_agent_id;
    }
    public void setSub_agent_id(int sub_agent_id) {
        this.sub_agent_id = sub_agent_id;
    }
    public int getShangwu_id() {
        return this.shangwu_id;
    }
    public void setShangwu_id(int shangwu_id) {
        this.shangwu_id = shangwu_id;
    }
    public int getNew_device() {
        return this.new_device;
    }
    public void setNew_device(int new_device) {
        this.new_device = new_device;
    }
    public int getLaunch() {
        return this.launch;
    }
    public void setLaunch(int launch) {
        this.launch = launch;
    }
    public int getZq_click() {
        return this.zq_click;
    }
    public void setZq_click(int zq_click) {
        this.zq_click = zq_click;
    }
    public int getZq_download() {
        return this.zq_download;
    }
    public void setZq_download(int zq_download) {
        this.zq_download = zq_download;
    }
    public int getTj_click() {
        return this.tj_click;
    }
    public void setTj_click(int tj_click) {
        this.tj_click = tj_click;
    }
    public int getTj_download() {
        return this.tj_download;
    }
    public void setTj_download(int tj_download) {
        this.tj_download = tj_download;
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
}
