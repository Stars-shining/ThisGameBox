package com.shentu.gamebox.bean;

public class AssistantBean {
    private String kf_number;

    private String kf_time;

    public String getKf_number() {
        return kf_number;
    }

    public void setKf_number(String kf_number) {
        this.kf_number = kf_number;
    }

    public String getKf_time() {
        return kf_time;
    }

    public void setKf_time(String kf_time) {
        this.kf_time = kf_time;
    }

    @Override
    public String toString() {
        return "AssistantBean{" +
                "kf_number='" + kf_number + '\'' +
                ", kf_time='" + kf_time + '\'' +
                '}';
    }
}
