package com.im.platform.common.interceptor.impl;

import com.im.platform.common.interceptor.BaseRuleChainService;
import com.im.platform.common.session.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 访问资源限制拦截器服务类
 * 继承BaseRuleChainService，基于滑动窗口对访问资源进行频率限制
 * 根据请求路径、用户ID、终端ID进行组合限流
 */
@Service
public class PathRuleChainService extends BaseRuleChainService {

    @Autowired
    private SlidingWindowLimitService slidingWindowLimitService;

    // 默认路径限流配置：1分钟内最多200次请求
    private static final long DEFAULT_WINDOW_SIZE_MS = 60000; // 1分钟
    private static final int DEFAULT_MAX_REQUESTS = 200;

    // 敏感路径限流配置：1分钟内最多50次请求
    private static final long SENSITIVE_WINDOW_SIZE_MS = 60000; // 1分钟
    private static final int SENSITIVE_MAX_REQUESTS = 50;

    // 高频操作限流配置：10秒内最多10次请求
    private static final long HIGH_FREQ_WINDOW_SIZE_MS = 10000; // 10秒
    private static final int HIGH_FREQ_MAX_REQUESTS = 10;

    // 敏感路径列表（需要更严格限制的路径）
    private static final String[] SENSITIVE_PATHS = {
        "/api/user/login",
        "/api/user/register",
        "/api/user/reset-password",
        "/api/user/send-sms",
        "/api/user/send-email",
        "/api/payment/",
        "/api/admin/",
        "/api/system/"
    };

    // 高频操作路径列表
    private static final String[] HIGH_FREQ_PATHS = {
        "/api/message/send",
        "/api/file/upload",
        "/api/search/query",
        "/api/data/export"
    };

@Override
    public boolean execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = request.getRequestURI();

        // 获取用户信息
        UserSession userSession = getUserSession(request);


        // 构建限流键：路径:用户ID:终端ID
        String limitKey = buildLimitKey(path, userSession.getUserId(), userSession.getTerminal().getCode());

        // 根据路径类型选择限流配置
        PathLimitConfig config = getLimitConfig(path);

        // 执行滑动窗口限流检查
        boolean allowed = slidingWindowLimitService.passThough(limitKey, config.windowSizeMs, config.maxRequests);

        if (!allowed) {
            // 被限流，拒绝请求
            handleRateLimit(request, response, limitKey, config);
            return false;
        }

        return true;
    }

    @Override
    public int getOrder() {
        // 资源限流应该在IP限流之后，认证之前执行
        return 4;
    }

    /**
     * 构建限流键
     * @param path 请求路径
     * @param userId 用户ID
     * @param terminalId 终端ID
     * @return 限流键
     */
    private String buildLimitKey(String path, Long userId, Integer terminalId) {
        // 对路径进行规范化处理，移除参数和版本号等
        String normalizedPath = normalizePath(path);
        return "path:" + normalizedPath + ":" + userId + ":" + terminalId;
    }

    /**
     * 规范化路径
     * @param path 原始路径
     * @return 规范化后的路径
     */
    private String normalizePath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return "/";
        }

        // 移除路径参数
        int paramIndex = path.indexOf("?");
        if (paramIndex != -1) {
            path = path.substring(0, paramIndex);
        }

        // 移除版本号（如 /api/v1/user -> /api/user）
        path = path.replaceAll("/v\\d+/", "/");

        // 移除ID参数（如 /api/user/123 -> /api/user）
        path = path.replaceAll("/\\d+(?=/|$)", "/:id");

        // 移除重复的斜杠
        path = path.replaceAll("/+", "/");

        // 确保以斜杠开头
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        return path;
    }

    /**
     * 根据路径获取限流配置
     * @param path 请求路径
     * @return 限流配置
     */
    private PathLimitConfig getLimitConfig(String path) {
        // 检查是否为高频操作路径
        for (String highFreqPath : HIGH_FREQ_PATHS) {
            if (path.startsWith(highFreqPath)) {
                return new PathLimitConfig(HIGH_FREQ_WINDOW_SIZE_MS, HIGH_FREQ_MAX_REQUESTS, "high_freq");
            }
        }

        // 检查是否为敏感路径
        for (String sensitivePath : SENSITIVE_PATHS) {
            if (path.startsWith(sensitivePath)) {
                return new PathLimitConfig(SENSITIVE_WINDOW_SIZE_MS, SENSITIVE_MAX_REQUESTS, "sensitive");
            }
        }

        // 使用默认配置
        return new PathLimitConfig(DEFAULT_WINDOW_SIZE_MS, DEFAULT_MAX_REQUESTS, "default");
    }

    /**
     * 处理被限流的请求
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param limitKey 限流键
     * @param config 限流配置
     */
    private void handleRateLimit(HttpServletRequest request, HttpServletResponse response,
                               String limitKey, PathLimitConfig config) throws Exception {
        // 设置响应状态和内容
        response.setStatus(429); // HTTP 429 Too Many Requests
        response.setContentType("application/json;charset=UTF-8");

        // 获取当前窗口内的请求计数
        long currentCount = slidingWindowLimitService.getCurrentCount(limitKey, config.windowSizeMs);

        String errorResponse = String.format(
            "{\"code\":429,\"message\":\"资源访问过于频繁，请稍后再试\",\"timestamp\":%d,\"path\":\"%s\",\"type\":\"%s\",\"limit\":\"%d requests per %d ms\",\"current\":%d}",
            System.currentTimeMillis(),
            request.getRequestURI(),
            config.type,
            config.maxRequests,
            config.windowSizeMs,
            currentCount
        );

        response.getWriter().write(errorResponse);

        // 记录限流日志
        logRateLimit(request, limitKey, config, currentCount);
    }

    /**
     * 记录限流日志
     * @param request HTTP请求对象
     * @param limitKey 限流键
     * @param config 限流配置
     * @param currentCount 当前计数
     */
    private void logRateLimit(HttpServletRequest request, String limitKey,
                             PathLimitConfig config, long currentCount) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");
        String ip = getIp(request);

        System.err.println("=== 资源访问限流触发 ===");
        System.err.println("时间: " + new java.util.Date());
        System.err.println("IP地址: " + ip);
        System.err.println("请求方法: " + method);
        System.err.println("请求路径: " + uri);
        System.err.println("限流键: " + limitKey);
        System.err.println("限流类型: " + config.type);
        System.err.println("User-Agent: " + userAgent);
        System.err.println("限流配置: " + config.maxRequests + " requests / " + config.windowSizeMs + " ms");
        System.err.println("当前计数: " + currentCount);

        // 如果是Redis实现，可以获取更详细的统计信息
        if (slidingWindowLimitService instanceof RedisSlidingWindowLimitService) {
            RedisSlidingWindowLimitService redisService = (RedisSlidingWindowLimitService) slidingWindowLimitService;
            String statistics = redisService.getStatistics(limitKey, config.windowSizeMs);
            System.err.println("详细统计: " + statistics);
        }

        System.err.println("=========================");
    }

    /**
     * 限流配置内部类
     */
    private static class PathLimitConfig {
        final long windowSizeMs;
        final int maxRequests;
        final String type;

        PathLimitConfig(long windowSizeMs, int maxRequests, String type) {
            this.windowSizeMs = windowSizeMs;
            this.maxRequests = maxRequests;
            this.type = type;
        }
    }
}

