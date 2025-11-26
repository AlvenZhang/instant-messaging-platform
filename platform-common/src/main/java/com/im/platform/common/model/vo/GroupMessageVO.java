package com.im.platform.common.model.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 群消息VO
 */
public class GroupMessageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    private Long id;

    /**
     * 群聊ID
     */
    private Long groupId;

    /**
     * 发送者ID
     */
    private Long sendId;

    /**
     * 发送者昵称
     */
    private String sendNickName;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息内容类型 具体枚举值由应用层定义
     */
    private Integer type;

    /**
     * @用户列表
     */
    private List<Long> atUserIds;

    /**
     * @用户列表
     */
    private String atUserIdsStr;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 发送时间
     */
    private Date sendTime;

    public GroupMessageVO() {
    }

    public GroupMessageVO(Long groupId, Long sendId, String content, Integer type) {
        this.groupId = groupId;
        this.sendId = sendId;
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

    public List<Long> getAtUserIds() {
        return atUserIds;
    }

    public void setAtUserIds(List<Long> atUserIds) {
        this.atUserIds = atUserIds;
    }

    public String getAtUserIdsStr() {
        return atUserIdsStr;
    }

    public void setAtUserIdsStr(String atUserIdsStr) {
        this.atUserIdsStr = atUserIdsStr;
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
        return "GroupMessageVO{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", sendId=" + sendId +
                ", sendNickName='" + sendNickName + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", atUserIds=" + atUserIds +
                ", atUserIdsStr='" + atUserIdsStr + '\'' +
                ", status=" + status +
                ", sendTime=" + sendTime +
                '}';
    }
}

