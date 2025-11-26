package com.im.platform.common.model.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 私聊消息VO
 */
public class PrivateMessageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    private Long id;

    /**
     * 发送者ID
     */
    private Long sendId;

    /**
     * 接收者ID
     */
    private Long recvId;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 消息内容类型 IMCmdType
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 发送时间
     */
    private Date sendTime;

    public PrivateMessageVO() {
    }

    public PrivateMessageVO(Long sendId, Long recvId, String content, Integer type) {
        this.sendId = sendId;
        this.recvId = recvId;
        this.content = content;
        this.type = type;
        this.sendTime = new Date();
    }

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

    @Override
    public String toString() {
        return "PrivateMessageVO{" +
                "id=" + id +
                ", sendId=" + sendId +
                ", recvId=" + recvId +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", sendTime=" + sendTime +
                '}';
    }
}

