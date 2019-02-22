package com.linyang.study.primary.architecture_components;

import androidx.annotation.NonNull;

/**
 * 描述:
 * Created by fzJiang on 2019/02/21 16:53 星期四
 */
public class User {

    private String userId;
    private String name;
    private String phone;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @NonNull
    @Override
    public String toString() {
        return "userId:" + userId + ",name:" + name + ",phone:" + phone;
    }
}
