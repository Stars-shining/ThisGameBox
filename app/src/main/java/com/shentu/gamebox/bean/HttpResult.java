package com.shentu.gamebox.bean;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class HttpResult<T> implements Serializable {
    /*{"ret":2,"msg":"\u8bf7\u6c42\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a","data":{}}*/
    private int ret;
    private String msg;
    private T data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "ret=" + ret +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
