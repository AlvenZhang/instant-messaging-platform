package com.im.platform.common.interceptor.impl;

/**
 * 滑动窗口限流服务接口
 * 定义滑动窗口限流的标准行为
 */
public interface SlidingWindowLimitService {

    /**
     * 检查是否能够通过滑动窗口的验证
     * @param key 限流的键（IP地址、资源路径等）
     * @param windowSizeMs 滑动窗口大小（毫秒）
     * @param maxRequests 窗口内最大允许的请求数
     * @return true表示允许通过，false表示被限流
     */
    boolean passThough(String key, long windowSizeMs, int maxRequests);

    /**
     * 检查是否能够通过滑动窗口的验证（使用默认配置）
     * @param key 限流的键
     * @return true表示允许通过，false表示被限流
     */
    default boolean passThough(String key) {
        // 默认配置：1分钟内最多60次请求
        return passThough(key, 60000, 60);
    }

    /**
     * 获取当前窗口内的请求计数
     * @param key 限流的键
     * @param windowSizeMs 滑动窗口大小（毫秒）
     * @return 当前窗口内的请求数
     */
    long getCurrentCount(String key, long windowSizeMs);

    /**
     * 重置指定键的限流计数
     * @param key 限流的键
     */
    void reset(String key);

    /**
     * 设置限流配置
     * @param key 限流的键
     * @param windowSizeMs 滑动窗口大小（毫秒）
     * @param maxRequests 窗口内最大允许的请求数
     */
    void setLimitConfig(String key, long windowSizeMs, int maxRequests);
}

