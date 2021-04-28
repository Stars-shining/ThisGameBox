package com.shentu.gamebox.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {@Index(value = "game_id DESC,date_int DESC",unique = true)})
public class boxGameData {
    @Id(autoincrement = true)
    private long id;
    private String agent_code;
    private int game_id;
    private int game_type;
    private int click;
    private int  download;
    private String date_str;
    private int date_int;
    @Generated(hash = 219515179)
    public boxGameData(long id, String agent_code, int game_id, int game_type,
            int click, int download, String date_str, int date_int) {
        this.id = id;
        this.agent_code = agent_code;
        this.game_id = game_id;
        this.game_type = game_type;
        this.click = click;
        this.download = download;
        this.date_str = date_str;
        this.date_int = date_int;
    }
    @Generated(hash = 967064991)
    public boxGameData() {
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
    public int getGame_id() {
        return this.game_id;
    }
    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }
    public int getGame_type() {
        return this.game_type;
    }
    public void setGame_type(int game_type) {
        this.game_type = game_type;
    }
    public int getClick() {
        return this.click;
    }
    public void setClick(int click) {
        this.click = click;
    }
    public int getDownload() {
        return this.download;
    }
    public void setDownload(int download) {
        this.download = download;
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
