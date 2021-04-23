package com.shentu.gamebox.http;

public class ApiException extends Exception{
    private int code;

    private String dispalyMessage;

    public ApiException( int code, String dispalyMessage) {
        this.code = code;
        this.dispalyMessage = dispalyMessage;
    }

    public ApiException(String message,  int code, String dispalyMessage) {
        super(message);
        this.code = code;
        this.dispalyMessage = dispalyMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDispalyMessage() {
        return dispalyMessage;
    }

    public void setDispalyMessage(String dispalyMessage) {
        this.dispalyMessage = dispalyMessage;
    }

}
