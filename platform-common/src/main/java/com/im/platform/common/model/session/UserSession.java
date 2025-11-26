package com.im.platform.common.model.session;

/**
 * 用户Session实体类
 * 用于存储当前请求用户的基本信息
 */
public class UserSession {
    private String userName;
    private String nickName;

    public UserSession() {
    }

    public UserSession(String userName, String nickName) {
        this.userName = userName;
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}

