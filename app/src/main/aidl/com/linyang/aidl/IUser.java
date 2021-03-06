package com.linyang.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

/**
 * 描述:存放与远端AIDL服务的实体类
 * Created by fzJiang on 2019-1-24
 */
public class IUser implements Parcelable {

    private String userID;
    private String userName;
    private String password;
    private String loginTime;

    public IUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public IUser(String userID, String userName, String password, String loginTime) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.loginTime = loginTime;
    }

    public IUser(Parcel in) {
        userID = in.readString();
        userName = in.readString();
        password = in.readString();
        loginTime = in.readString();
    }

    public static final Creator<IUser> CREATOR = new Creator<IUser>() {

        @Override
        public IUser createFromParcel(Parcel in) {
            return new IUser(in);
        }

        @Override
        public IUser[] newArray(int size) {
            return new IUser[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeString(loginTime);
    }

    /**
     * 输出反序列化
     *
     * @param out 输出
     */
    public void readFromParcel(Parcel out) {
        userID = out.readString();
        userName = out.readString();
        password = out.readString();
        loginTime = out.readString();
    }

    @NotNull
    @Override
    public String toString() {
        return "userID:" + userID + ",userName:" + userName + ",password:" + password + ",loginTime:" + loginTime;
    }
}
