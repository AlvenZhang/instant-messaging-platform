package com.im.platform.common.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 在线终端VO
 */
public class OnlineTerminalVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 在线终端类型
     */
    private List<Integer> terminals;

    public OnlineTerminalVO() {
    }

    public OnlineTerminalVO(Long userId, List<Integer> terminals) {
        this.userId = userId;
        this.terminals = terminals;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Integer> getTerminals() {
        return terminals;
    }

    public void setTerminals(List<Integer> terminals) {
        this.terminals = terminals;
    }

    @Override
    public String toString() {
        return "OnlineTerminalVO{" +
                "userId=" + userId +
                ", terminals=" + terminals +
                '}';
    }
}

