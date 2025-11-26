package com.im.platform.common.interceptor.impl;

import com.im.platform.common.interceptor.BaseRuleChainService;
import com.im.platform.common.session.UserSession;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 账号安全校验拦截器服务类
 * 继承BaseRuleChainService，用于验证用户是否已登录
 */
@Service
public class AuthRuleChainService extends BaseRuleChainService {

    @Override
    public boolean execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取用户session（通过BaseRuleChainService的getUserSession方法）
        UserSession userSession = getUserSession(request);

        if (userSession == null) {
            // 用户未登录，返回401未授权错误
            handleUnauthorized(request, response, "用户未登录，请先登录");
            return false;
        }

        // 检查用户名是否有效
        if (!StringUtils.hasText(userSession.getUserName())) {
            // 用户信息无效，返回403禁止访问错误
            handleForbidden(request, response, "用户信息无效，请重新登录");
            return false;
        }

        // 检查用户昵称是否有效（可选）
        if (!StringUtils.hasText(userSession.getNickName())) {
            // 可以选择允许访问，或者要求更完整的信息
            System.out.println("警告: 用户 " + userSession.getUserName() + " 昵称信息缺失");
        }

        // 记录用户访问日志
        logUserAccess(request, userSession);

        return true;
    }

    @Override
    public int getOrder() {
        // 认证服务应该在XSS检查、限流之后执行，优先级设置为10
        return 10;
    }

    /**
     * 处理未授权访问
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param message 错误消息
     */
    private void handleUnauthorized(HttpServletRequest request, HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        String errorResponse = String.format(
            "{\"code\":401,\"message\":\"%s\",\"timestamp\":%d,\"path\":\"%s\"}",
            message,
            System.currentTimeMillis(),
            request.getRequestURI()
        );

        response.getWriter().write(errorResponse);

        // 记录未授权访问日志
        logUnauthorizedAccess(request, message);
    }

    /**
     * 处理禁止访问
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param message 错误消息
     */
    private void handleForbidden(HttpServletRequest request, HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        String errorResponse = String.format(
            "{\"code\":403,\"message\":\"%s\",\"timestamp\":%d,\"path\":\"%s\"}",
            message,
            System.currentTimeMillis(),
            request.getRequestURI()
        );

        response.getWriter().write(errorResponse);

        // 记录禁止访问日志
        logForbiddenAccess(request, message);
    }

    /**
     * 记录用户访问日志
     * @param request HTTP请求对象
     * @param userSession 用户会话信息
     */
    private void logUserAccess(HttpServletRequest request, UserSession userSession) {
        String ip = getIp(request);
        String userName = userSession.getUserName();
        String nickName = userSession.getNickName();
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String userAgent = request.getHeader("User-Agent");

        System.out.println("=== 用户访问记录 ===");
        System.out.println("时间: " + new java.util.Date());
        System.out.println("用户名: " + userName);
        System.out.println("用户昵称: " + nickName);
        System.out.println("IP地址: " + ip);
        System.out.println("请求方法: " + method);
        System.out.println("请求路径: " + requestURI);
        System.out.println("User-Agent: " + userAgent);
        System.out.println("==================");
    }

    /**
     * 记录未授权访问日志
     * @param request HTTP请求对象
     * @param message 错误消息
     */
    private void logUnauthorizedAccess(HttpServletRequest request, String message) {
        String ip = getIp(request);
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String userAgent = request.getHeader("User-Agent");

        System.err.println("=== 未授权访问记录 ===");
        System.err.println("时间: " + new java.util.Date());
        System.err.println("IP地址: " + ip);
        System.err.println("请求方法: " + method);
        System.err.println("请求路径: " + requestURI);
        System.err.println("错误信息: " + message);
        System.err.println("User-Agent: " + userAgent);
        System.err.println("====================");
    }

    /**
     * 记录禁止访问日志
     * @param request HTTP请求对象
     * @param message 错误消息
     */
    private void logForbiddenAccess(HttpServletRequest request, String message) {
        String ip = getIp(request);
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String userAgent = request.getHeader("User-Agent");

        System.err.println("=== 禁止访问记录 ===");
        System.err.println("时间: " + new java.util.Date());
        System.err.println("IP地址: " + ip);
        System.err.println("请求方法: " + method);
        System.err.println("请求路径: " + requestURI);
        System.err.println("错误信息: " + message);
        System.err.println("User-Agent: " + userAgent);
        System.err.println("==================");
    }

}

