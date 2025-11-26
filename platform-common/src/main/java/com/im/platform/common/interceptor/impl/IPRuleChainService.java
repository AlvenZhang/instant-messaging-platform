package com.im.platform.common.interceptor.impl;

import com.im.platform.common.interceptor.BaseRuleChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * IP限制拦截器服务类
 * 继承BaseRuleChainService，基于滑动窗口对IP进行访问频率限制
 */
@Service
public class IPRuleChainService extends BaseRuleChainService {

    @Autowired
    private SlidingWindowLimitService slidingWindowLimitService;

    // 默认IP限流配置：1分钟内最多100次请求
    private static final long DEFAULT_WINDOW_SIZE_MS = 60000; // 1分钟
    private static final int DEFAULT_MAX_REQUESTS = 100;

    // 特殊IP配置（可以配置为更严格或更宽松的限制）
    private static final long SPECIAL_WINDOW_SIZE_MS = 30000; // 30秒
    private static final int SPECIAL_MAX_REQUESTS = 20;

    @Override
    public boolean execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ip = getIp(request);

        // 获取IP，如果无法获取则放行
        if (ip == null || "unknown".equals(ip) || ip.trim().isEmpty()) {
            return true;
        }

        // 检查是否为特殊IP（内网IP等）
        boolean isSpecialIp = isSpecialIp(ip);

        // 选择限流配置
        long windowSizeMs = isSpecialIp ? SPECIAL_WINDOW_SIZE_MS : DEFAULT_WINDOW_SIZE_MS;
        int maxRequests = isSpecialIp ? SPECIAL_MAX_REQUESTS : DEFAULT_MAX_REQUESTS;

        // 执行滑动窗口限流检查
        boolean allowed = slidingWindowLimitService.passThough(ip, windowSizeMs, maxRequests);

        if (!allowed) {
            // 被限流，拒绝请求
            handleRateLimit(request, response, ip, windowSizeMs, maxRequests);
            return false;
        }

        return true;
    }

    @Override
    public int getOrder() {
        // IP限流应该在XSS检查之后，认证之前执行
        return 3;
    }

    /**
     * 判断是否为特殊IP（内网IP、白名单IP等）
     * @param ip IP地址
     * @return true表示是特殊IP
     */
    private boolean isSpecialIp(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return false;
        }

        // 内网IP地址范围
        return ip.startsWith("192.168.") ||
               ip.startsWith("10.") ||
               ip.startsWith("172.") ||
               ip.equals("127.0.0.1") ||
               ip.equals("0:0:0:0:0:0:0:1") || // IPv6本地地址
               ip.startsWith("169.254."); // 链路本地地址
    }

    /**
     * 处理被限流的请求
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param ip 被限流的IP
     * @param windowSizeMs 窗口大小
     * @param maxRequests 最大请求数
     */
    private void handleRateLimit(HttpServletRequest request, HttpServletResponse response,
                               String ip, long windowSizeMs, int maxRequests) throws Exception {
        // 设置响应状态和内容
        response.setStatus(429); // HTTP 429 Too Many Requests
        response.setContentType("application/json;charset=UTF-8");

        // 获取当前窗口内的请求计数
        long currentCount = slidingWindowLimitService.getCurrentCount(ip, windowSizeMs);

        String errorResponse = String.format(
            "{\"code\":429,\"message\":\"请求过于频繁，请稍后再试\",\"timestamp\":%d,\"path\":\"%s\",\"ip\":\"%s\",\"limit\":\"%d requests per %d ms\",\"current\":%d}",
            System.currentTimeMillis(),
            request.getRequestURI(),
            ip,
            maxRequests,
            windowSizeMs,
            currentCount
        );

        response.getWriter().write(errorResponse);

        // 记录限流日志
        logRateLimit(request, ip, windowSizeMs, maxRequests, currentCount);
    }

    /**
     * 记录限流日志
     * @param request HTTP请求对象
     * @param ip 被限流的IP
     * @param windowSizeMs 窗口大小
     * @param maxRequests 最大请求数
     * @param currentCount 当前计数
     */
    private void logRateLimit(HttpServletRequest request, String ip, long windowSizeMs,
                             int maxRequests, long currentCount) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");

        System.err.println("=== IP限流触发 ===");
        System.err.println("时间: " + new java.util.Date());
        System.err.println("IP地址: " + ip);
        System.err.println("请求方法: " + method);
        System.err.println("请求路径: " + uri);
        System.err.println("User-Agent: " + userAgent);
        System.err.println("限流配置: " + maxRequests + " requests / " + windowSizeMs + " ms");
        System.err.println("当前计数: " + currentCount);

        // 如果是Redis实现，可以获取更详细的统计信息
        if (slidingWindowLimitService instanceof RedisSlidingWindowLimitService) {
            RedisSlidingWindowLimitService redisService = (RedisSlidingWindowLimitService) slidingWindowLimitService;
            String statistics = redisService.getStatistics(ip, windowSizeMs);
            System.err.println("详细统计: " + statistics);
        }

        System.err.println("=================");
    }

    /**
     * 重置指定IP的限流计数（管理接口）
     * @param ip IP地址
     */
    public void resetIpLimit(String ip) {
        if (ip != null && !ip.trim().isEmpty()) {
            slidingWindowLimitService.reset(ip);
            System.out.println("已重置IP " + ip + " 的限流计数");
        }
    }

    /**
     * 获取IP限流统计信息
     * @param ip IP地址
     * @return 统计信息
     */
    public String getIpStatistics(String ip) {
        if (slidingWindowLimitService instanceof RedisSlidingWindowLimitService) {
            RedisSlidingWindowLimitService redisService = (RedisSlidingWindowLimitService) slidingWindowLimitService;
            return redisService.getStatistics(ip, DEFAULT_WINDOW_SIZE_MS);
        }
        return "当前计数: " + slidingWindowLimitService.getCurrentCount(ip, DEFAULT_WINDOW_SIZE_MS);
    }
}

