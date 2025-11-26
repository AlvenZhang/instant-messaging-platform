package com.im.platform.common.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 邀请好友进群请求VO
 */
public class GroupInviteVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 群ID
     */
    private Long groupId;

    /**
     * 好友ID列表
     */
    private List<Long> friendIds;

    public GroupInviteVO() {
    }

    public GroupInviteVO(Long groupId, List<Long> friendIds) {
        this.groupId = groupId;
        this.friendIds = friendIds;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<Long> getFriendIds() {
        return friendIds;
    }

    public void setFriendIds(List<Long> friendIds) {
        this.friendIds = friendIds;
    }

    @Override
    public String toString() {
        return "GroupInviteVO{" +
                "groupId=" + groupId +
                ", friendIds=" + friendIds +
                '}';
    }
}

