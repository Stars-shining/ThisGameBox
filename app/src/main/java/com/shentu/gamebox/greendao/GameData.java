package com.shentu.gamebox.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

@Entity(indexes = {@Index(value = "UUID DESC,agent_code DESC,gameId DESC",unique = true)})
public class GameData {
    @Id(autoincrement =  true)
    private long id;
    private String UUID;
    private String gameId;
    private String downloadCount;
    private String currentTiem;
    private String InstallTime;
    private String agent_code;
    private String type;


    public GameData() {
    }

    @Generated(hash = 2983445)
    public GameData(long id, String UUID, String gameId, String downloadCount,
            String currentTiem, String InstallTime, String agent_code, String type) {
        this.id = id;
        this.UUID = UUID;
        this.gameId = gameId;
        this.downloadCount = downloadCount;
        this.currentTiem = currentTiem;
        this.InstallTime = InstallTime;
        this.agent_code = agent_code;
        this.type = type;
    }

    public String getUUID() {
        return this.UUID;
    }
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
    public String getGameId() {
        return this.gameId;
    }
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
    public String getDownloadCount() {
        return this.downloadCount;
    }
    public void setDownloadCount(String downloadCount) {
        this.downloadCount = downloadCount;
    }
    public String getCurrentTiem() {
        return this.currentTiem;
    }
    public void setCurrentTiem(String currentTiem) {
        this.currentTiem = currentTiem;
    }
    public String getAgent_code() {
        return this.agent_code;
    }
    public void setAgent_code(String agent_code) {
        this.agent_code = agent_code;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInstallTime() {
        return this.InstallTime;
    }

    public void setInstallTime(String InstallTime) {
        this.InstallTime = InstallTime;
    }

}
