package com.shentu.gamebox.bean;

import java.io.Serializable;
import java.net.URL;

public class BannerBean implements Serializable {

        private String id;
        private String game_id;
        private String img;
        private String status;
        private String list_order;
        private String add_time;
        private String game_name;
        private String game_intro;
        private String game_icon;
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setGame_id(String game_id) {
            this.game_id = game_id;
        }
        public String getGame_id() {
            return game_id;
        }

        public void setImg(String img) {
            this.img = img;
        }
        public String getImg() {
            return img;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public String getStatus() {
            return status;
        }

        public void setList_order(String list_order) {
            this.list_order = list_order;
        }
        public String getList_order() {
            return list_order;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
        public String getAdd_time() {
            return add_time;
        }

        public void setGame_name(String game_name) {
            this.game_name = game_name;
        }
        public String getGame_name() {
            return game_name;
        }

        public void setGame_intro(String game_intro) {
            this.game_intro = game_intro;
        }
        public String getGame_intro() {
            return game_intro;
        }

        public void setGame_icon(String game_icon) {
            this.game_icon = game_icon;
        }
        public String getGame_icon() {
            return game_icon;
        }

    @Override
    public String toString() {
        return "BannerBean{" +
                "id='" + id + '\'' +
                ", game_id='" + game_id + '\'' +
                ", img=" + img +
                ", status='" + status + '\'' +
                ", list_order='" + list_order + '\'' +
                ", add_time='" + add_time + '\'' +
                ", game_name='" + game_name + '\'' +
                ", game_intro='" + game_intro + '\'' +
                ", game_icon='" + game_icon + '\'' +
                '}';
    }
}
