package com.im.platform.common.model.vo;

import java.io.Serializable;

/**
 * 群成员信息VO
 */
public class GroupMemberVO extends GroupMemberSimpleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 是否在线
     */
    private Boolean online;

    /**
     * 备注
     */
    private String remark;

    public GroupMemberVO() {
    }

    public GroupMemberVO(Long userId, String aliasName, String headImage, Boolean quit, Boolean online, String remark) {
        super();
        this.userId = userId;
        this.setAliasName(aliasName);
        this.setQuit(quit);
        this.headImage = headImage;
        this.online = online;
        this.remark = remark;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "GroupMemberVO{" +
                "userId=" + userId +
                ", aliasName='" + getAliasName() + '\'' +
                ", headImage='" + headImage + '\'' +
                ", quit=" + getQuit() +
                ", online=" + online +
                ", remark='" + remark + '\'' +
                ", groupId=" + getGroupId() +
                ", createdTime=" + getCreatedTime() +
                '}';
    }
}

