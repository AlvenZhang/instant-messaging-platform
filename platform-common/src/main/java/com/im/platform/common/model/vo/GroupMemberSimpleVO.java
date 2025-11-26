package com.im.platform.common.model.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 群成员简易信息VO
 */
public class GroupMemberSimpleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 群内显示名称
     */
    private String aliasName;

    /**
     * 是否已退出
     */
    private Boolean quit;

    /**
     * 群组ID
     */
    private Long groupId;

    /**
     * 创建时间
     */
    private Date createdTime;

    public GroupMemberSimpleVO() {
    }

    public GroupMemberSimpleVO(String aliasName, Boolean quit, Long groupId, Date createdTime) {
        this.aliasName = aliasName;
        this.quit = quit;
        this.groupId = groupId;
        this.createdTime = createdTime;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Boolean getQuit() {
        return quit;
    }

    public void setQuit(Boolean quit) {
        this.quit = quit;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "GroupMemberSimpleVO{" +
                "aliasName='" + aliasName + '\'' +
                ", quit=" + quit +
                ", groupId=" + groupId +
                ", createdTime=" + createdTime +
                '}';
    }
}

