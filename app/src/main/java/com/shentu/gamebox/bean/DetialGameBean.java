package com.shentu.gamebox.bean;

import java.io.Serializable;

public class DetialGameBean implements Serializable {

        private String user_id;
        private String type;
        private String name;
        private String icon;
        private String intro;
        private String open_time;
        private String imgs;
        private String version_intro;
        private String open_close;
        private String gift;
        private String gift_detail;
        private String rebate;
        private String vip_open;
        private String status;
        private String display;
        private String cover;
        private String video;

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
        public String getUser_id() {
            return user_id;
        }

        public void setType(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
        public String getIcon() {
            return icon;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }
        public String getIntro() {
            return intro;
        }

        public void setOpen_time(String open_time) {
            this.open_time = open_time;
        }
        public String getOpen_time() {
            return open_time;
        }

        public void setImgs(String imgs) {
            this.imgs = imgs;
        }
        public String getImgs() {
            return imgs;
        }

        public void setVersion_intro(String version_intro) {
            this.version_intro = version_intro;
        }
        public String getVersion_intro() {
            return version_intro;
        }

        public void setOpen_close(String open_close) {
            this.open_close = open_close;
        }
        public String getOpen_close() {
            return open_close;
        }

        public void setGift(String gift) {
            this.gift = gift;
        }
        public String getGift() {
            return gift;
        }

        public void setGift_detail(String gift_detail) {
            this.gift_detail = gift_detail;
        }
        public String getGift_detail() {
            return gift_detail;
        }

        public void setRebate(String rebate) {
            this.rebate = rebate;
        }
        public String getRebate() {
            return rebate;
        }

    public String getVip_open() {
        return vip_open;
    }

    public void setVip_open(String vip_open) {
        this.vip_open = vip_open;
    }

    public void setStatus(String status) {
            this.status = status;
        }
        public String getStatus() {
            return status;
        }

        public void setDisplay(String display) {
            this.display = display;
        }
        public String getDisplay() {
            return display;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }
        public String getCover() {
            return cover;
        }

        public void setVideo(String video) {
            this.video = video;
        }
        public String getVideo() {
            return video;
        }

    @Override
    public String toString() {
        return "DetialGameBean{" +
                "user_id='" + user_id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", intro='" + intro + '\'' +
                ", open_time='" + open_time + '\'' +
                ", imgs='" + imgs + '\'' +
                ", version_intro='" + version_intro + '\'' +
                ", open_close='" + open_close + '\'' +
                ", gift='" + gift + '\'' +
                ", gift_detail='" + gift_detail + '\'' +
                ", rebate='" + rebate + '\'' +
                ", vip_open='" + vip_open + '\'' +
                ", status='" + status + '\'' +
                ", display='" + display + '\'' +
                ", cover='" + cover + '\'' +
                ", video='" + video + '\'' +
                '}';
    }
}
