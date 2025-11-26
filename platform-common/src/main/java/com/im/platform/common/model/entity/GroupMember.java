package com.im.platform.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 群成员实体类
 * 对应数据库表：group_member
 */
@TableName("group_member")
public class GroupMember {
    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;
    /**
     * 群ID
     */
    @TableField("group_id")
    private Long groupId;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 群内显示名称
     */
    @TableField("alias_name")
    private String aliasName;
    /**
     * 头像
     */
    @TableField("head_image")
    private String headImage;
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    /**
     * 是否已离开群聊
     */
    @TableField("quit")
    private Boolean quit;
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getQuit() {
        return quit;
    }

    public void setQuit(Boolean quit) {
        this.quit = quit;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}

