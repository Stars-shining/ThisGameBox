package com.shentu.gamebox.bean;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.List;

@Keep
public class GameBean<T> implements Serializable {
    /*{"ret":2,"msg":"","data":{list[{}],count:""}}*/
    private int count;

    private List<T> list;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
