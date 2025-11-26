package com.im.platform.common.interceptor.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis滑动窗口限流实现类
 * 使用Redis的ZSET数据结构实现滑动窗口限流
 */
@Service
public class RedisSlidingWindowLimitService implements SlidingWindowLimitService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Redis键前缀
    private static final String RATE_LIMIT_PREFIX = "rate_limit:";

    @Override
    public boolean passThough(String key, long windowSizeMs, int maxRequests) {
        if (key == null || key.trim().isEmpty()) {
            return false;
        }

        String redisKey = RATE_LIMIT_PREFIX + key;
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - windowSizeMs;

        try {
            // 使用更简单的方式实现滑动窗口限流
            ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();

            // 移除窗口外的旧记录
            zSetOps.removeRangeByScore(redisKey, 0, windowStart);

            // 获取当前窗口内的请求数
            Long currentCount = zSetOps.count(redisKey, windowStart, currentTime);

            // 检查是否超过限制
            if (currentCount != null && currentCount >= maxRequests) {
                return false;
            }

            // 添加当前请求记录
            zSetOps.add(redisKey, String.valueOf(currentTime), currentTime);

            // 设置过期时间，防止Redis中键过多
            redisTemplate.expire(redisKey, windowSizeMs, TimeUnit.MILLISECONDS);

            return true;

        } catch (Exception e) {
            // Redis异常时，为了不影响业务，可以选择放行或拒绝
            // 这里选择记录日志并放行
            System.err.println("Redis滑动窗口限流异常: " + e.getMessage());
            return true; // 异常时放行，避免影响业务
        }
    }

    @Override
    public long getCurrentCount(String key, long windowSizeMs) {
        if (key == null || key.trim().isEmpty()) {
            return 0;
        }

        String redisKey = RATE_LIMIT_PREFIX + key;
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - windowSizeMs;

        try {
            ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
            Long count = zSetOps.count(redisKey, windowStart, currentTime);
            return count != null ? count : 0;
        } catch (Exception e) {
            System.err.println("获取当前窗口计数异常: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public void reset(String key) {
        if (key == null || key.trim().isEmpty()) {
            return;
        }

        String redisKey = RATE_LIMIT_PREFIX + key;
        try {
            redisTemplate.delete(redisKey);
        } catch (Exception e) {
            System.err.println("重置限流计数异常: " + e.getMessage());
        }
    }

    @Override
    public void setLimitConfig(String key, long windowSizeMs, int maxRequests) {
        // Redis实现的限流配置是通过passThough方法的参数动态设置的
        // 这里可以存储配置信息供查询使用
        String configKey = RATE_LIMIT_PREFIX + "config:" + key;
        String configValue = windowSizeMs + ":" + maxRequests;

        try {
            redisTemplate.opsForValue().set(configKey, configValue, 24, TimeUnit.HOURS);
        } catch (Exception e) {
            System.err.println("设置限流配置异常: " + e.getMessage());
        }
    }
    /**
     * 获取详细的统计信息
     * @param key 限流的键
     * @param windowSizeMs 滑动窗口大小（毫秒）
     * @return 统计信息字符串
     */
    public String getStatistics(String key, long windowSizeMs) {
        if (key == null || key.trim().isEmpty()) {
            return "无效的键";
        }

        String redisKey = RATE_LIMIT_PREFIX + key;
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - windowSizeMs;

        try {
            ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
            Long totalCount = zSetOps.count(redisKey, windowStart, currentTime);

            // 获取最早的请求时间
            var earliest = zSetOps.range(redisKey, 0, 0);
            String earliestTime = earliest != null && !earliest.isEmpty() ? earliest.iterator().next() : "无";

            // 获取最新的请求时间
            var latest = zSetOps.range(redisKey, -1, -1);
            String latestTime = latest != null && !latest.isEmpty() ? latest.iterator().next() : "无";

            return String.format(
                "键: %s, 当前窗口请求数: %d, 最早请求时间: %s, 最新请求时间: %s",
                key, totalCount, earliestTime, latestTime
            );
        } catch (Exception e) {
            System.err.println("获取统计信息异常: " + e.getMessage());
            return "获取统计信息失败: " + e.getMessage();
        }
    }
}

