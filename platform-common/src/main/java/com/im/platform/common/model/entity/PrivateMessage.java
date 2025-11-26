package com.im.platform.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 私聊消息实体类
 * 对应数据库表：private_message
 */
@TableName("private_message")
public class PrivateMessage {
    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;
    /**
     * 发送用户ID
     */
    @TableField("send_id")
    private Long sendId;
    /**
     * 接收用户ID
     */
    @TableField("recv_id")
    private Long recvId;
    /**
     * 发送内容
     */
    @TableField("content")
    private String content;
    /**
     * 消息类型 0:文字 1:图片 2:文件 3:语音 10:撤回消息
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

    public Long getSendId() {
        return sendId;
    }

    public void setSendId(Long sendId) {
        this.sendId = sendId;
    }

    public Long getRecvId() {
        return recvId;
    }

    public void setRecvId(Long recvId) {
        this.recvId = recvId;
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

