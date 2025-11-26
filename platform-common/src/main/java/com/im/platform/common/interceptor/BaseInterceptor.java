package com.im.platform.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;

/**
 * 拦截器抽象类
 * 实现HandlerInterceptor接口，提供拦截器链服务的管理和执行
 */
@Component
public abstract class BaseInterceptor implements HandlerInterceptor {

    /**
     * 自动注入所有已定义的拦截器链服务
     */
    @Autowired
    protected List<RuleChainService> ruleChainServices;

    /**
     * 获取所有已注入的拦截器链服务，并按优先级排序
     * @return 排序后的拦截器链服务列表
     */
    protected List<RuleChainService> getRuleChainServices() {
        if (ruleChainServices == null || ruleChainServices.isEmpty()) {
            return List.of();
        }

        // 按order值升序排序，数值越小优先级越高
        return ruleChainServices.stream()
                .sorted(Comparator.comparingInt(RuleChainService::getOrder))
                .toList();
    }

    /**
     * 检查请求路径是否需要被拦截
     * @param request HTTP请求对象
     * @param excludePaths 不需要拦截的路径数组
     * @return true表示需要拦截，false表示不需要拦截
     */
    protected boolean shouldIntercept(HttpServletRequest request, String[] excludePaths) {
        String requestURI = request.getRequestURI();

        if (excludePaths == null || excludePaths.length == 0) {
            return true;
        }

        for (String excludePath : excludePaths) {
            if (requestURI.startsWith(excludePath)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查请求路径是否匹配指定的路径
     * @param request HTTP请求对象
     * @param path 要检查的路径
     * @return true表示匹配，false表示不匹配
     */
    protected boolean isPathMatch(HttpServletRequest request, String path) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith(path);
    }

    /**
     * 设置响应错误信息
     * @param response HTTP响应对象
     * @param status HTTP状态码
     * @param message 错误信息
     */
    protected void setErrorResponse(HttpServletResponse response, int status, String message) {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        try {
            String jsonResponse = String.format(
                "{\"code\":%d,\"message\":\"%s\",\"timestamp\":%d}",
                status,
                message,
                System.currentTimeMillis()
            );
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            // 忽略写入响应时的异常
        }
    }
}

