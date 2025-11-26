package com.im.platform.common.model.vo;

import java.io.Serializable;

/**
 * 群信息VO
 */
public class GroupVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 群ID
     */
    private Long id;

    /**
     * 群名称
     */
    private String name;

    /**
     * 群主ID
     */
    private Long ownerId;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 头像缩略图
     */
    private String headImageThumb;

    /**
     * 群公告
     */
    private String notice;

    /**
     * 用户在群显示昵称
     */
    private String aliasName;

    /**
     * 群聊显示备注
     */
    private String remark;

    public GroupVO() {
    }

    public GroupVO(Long id, String name, Long ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "GroupVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ownerId=" + ownerId +
                ", headImage='" + headImage + '\'' +
                ", headImageThumb='" + headImageThumb + '\'' +
                ", notice='" + notice + '\'' +
                ", aliasName='" + aliasName + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}

