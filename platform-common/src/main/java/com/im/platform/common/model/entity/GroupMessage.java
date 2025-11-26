package com.im.platform.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 群消息实体类
 * 对应数据库表：group_message
 */
@TableName("group_message")
public class GroupMessage {
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
     * 发送用户ID
     */
    @TableField("send_id")
    private Long sendId;
    /**
     * 发送用户昵称
     */
    @TableField("send_nick_name")
    private String sendNickName;
    /**
     * @用户列表
     */
    @TableField("at_user_ids")
    private String atUserIds;
    /**
     * 发送内容
     */
    @TableField("content")
    private String content;
    /**
     * 消息类型 0:文字 1:图片 2:文件
     */
    @TableField("type")
    private Integer type;
    /**
     * 状态
     */
    @TableField("status")
    private Integer status;
    /**
     * 发送时间
     */
    @TableField("send_time")
    private Date sendTime;

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

    public Long getSendId() {
        return sendId;
    }

    public void setSendId(Long sendId) {
        this.sendId = sendId;
    }

    public String getSendNickName() {
        return sendNickName;
    }

    public void setSendNickName(String sendNickName) {
        this.sendNickName = sendNickName;
    }

    public String getAtUserIds() {
        return atUserIds;
    }

    public void setAtUserIds(String atUserIds) {
        this.atUserIds = atUserIds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}

