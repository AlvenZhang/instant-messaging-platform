package com.im.platform.common.model.vo;

import java.io.Serializable;

/**
 * 用户信息VO
 */
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 用户类型 1:普通用户 2:审核账户
     */
    private Integer type;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 头像缩略图
     */
    private String headImageThumb;

    /**
     * 是否在线
     */
    private Boolean online;

    public UserVO() {
    }

    public UserVO(Long id, String userName, String nickName) {
        this.id = id;
        this.userName = userName;
        this.nickName = nickName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getHeadImageThumb() {
        return headImageThumb;
    }

    public void setHeadImageThumb(String headImageThumb) {
        this.headImageThumb = headImageThumb;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                ", type=" + type +
                ", signature='" + signature + '\'' +
                ", headImage='" + headImage + '\'' +
                ", headImageThumb='" + headImageThumb + '\'' +
                ", online=" + online +
                '}';
    }
}

