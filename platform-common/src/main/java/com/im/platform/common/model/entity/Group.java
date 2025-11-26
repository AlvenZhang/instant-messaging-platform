package com.im.platform.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 群组实体类
 * 对应数据库表：group
 */
@TableName("group")
public class Group {
    /**
     * 群组ID
     */
    @TableId("id")
    private Long id;
    /**
     * 群名称
     */
    @TableField("name")
    private String name;
    /**
     * 群主ID
     */
    @TableField("owner_id")
    private Long ownerId;
    /**
     * 头像
     */
    @TableField("head_image")
    private String headImage;
    /**
     * 头像缩略图
     */
    @TableField("head_image_thumb")
    private String headImageThumb;
    /**
     * 群公告
     */
    @TableField("notice")
    private String notice;
    /**
     * 是否已删除
     */
    @TableField("deleted")
    private Boolean deleted;
    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}

