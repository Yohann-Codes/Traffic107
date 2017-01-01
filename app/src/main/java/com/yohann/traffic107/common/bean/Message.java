package com.yohann.traffic107.common.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Yohann on 2016/8/30.
 */
public class Message extends BmobObject {
    private String username;
    private String msg;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUsername() {
        return username;
    }

    public String getMsg() {
        return msg;
    }
}
