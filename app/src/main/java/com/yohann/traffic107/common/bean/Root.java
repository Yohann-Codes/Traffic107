package com.yohann.traffic107.common.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Yohann on 2016/8/25.
 */
public class Root extends BmobObject {
    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
