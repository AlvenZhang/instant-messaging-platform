package com.im.platform.common.model.vo;

import java.io.Serializable;

/**
 * 好友信息VO
 */
public class FriendVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 好友ID
     */
    private Long id;

    /**
     * 好友昵称
     */
    private String nickName;

    /**
     * 好友头像
     */
    private String headImage;

    public FriendVO() {
    }

    public FriendVO(Long id, String nickName, String headImage) {
        this.id = id;
        this.nickName = nickName;
        this.headImage = headImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    @Override
    public String toString() {
        return "FriendVO{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", headImage='" + headImage + '\'' +
                '}';
    }
}

