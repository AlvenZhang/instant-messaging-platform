package com.im.platform.common.interceptor.impl;

import com.im.platform.common.interceptor.BaseRuleChainService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * XSS漏洞校验拦截器服务
 * 继承BaseRuleChainService，用于检测和防范XSS攻击
 */
@Service
public class XssRuleChainService extends BaseRuleChainService {

    @Override
    public boolean execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 使用XssUtils检查请求中是否包含XSS攻击
        if (XssUtils.checkXss(request)) {
            // 检测到XSS攻击，拒绝请求
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json;charset=UTF-8");

            String errorResponse = String.format(
                "{\"code\":%d,\"message\":\"请求包含恶意脚本，已被拒绝\",\"timestamp\":%d,\"path\":\"%s\"}",
                HttpServletResponse.SC_BAD_REQUEST,
                System.currentTimeMillis(),
                request.getRequestURI()
            );

            response.getWriter().write(errorResponse);

            // 记录XSS攻击日志
            logXssAttack(request);

            return false;
        }

        return true;
    }

    @Override
    public int getOrder() {
        // XSS检查应该有较高优先级，在业务逻辑执行前进行
        return 5;
    }

    /**
     * 记录XSS攻击日志
     * @param request HTTP请求对象
     */
    private void logXssAttack(HttpServletRequest request) {
        String ip = getIp(request);
        String userAgent = request.getHeader("User-Agent");
        String method = request.getMethod();
        String uri = request.getRequestURI();

        System.err.println("=== XSS攻击检测 ===");
        System.err.println("时间: " + new java.util.Date());
        System.err.println("IP地址: " + ip);
        System.err.println("请求方法: " + method);
        System.err.println("请求路径: " + uri);
        System.err.println("User-Agent: " + userAgent);

        // 记录请求参数（脱敏处理）
        try {
            java.util.Map<String, String[]> parameterMap = request.getParameterMap();
            if (!parameterMap.isEmpty()) {
                System.err.println("请求参数:");
                for (java.util.Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    String paramName = entry.getKey();
                    String[] paramValues = entry.getValue();
                    if (paramValues != null) {
                        for (String value : paramValues) {
                            // 记录参数值的前50个字符，避免日志过长
                            String maskedValue = value.length() > 50 ?
                                value.substring(0, 50) + "..." : value;
                            System.err.println("  " + paramName + ": " + maskedValue);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("记录请求参数时发生异常: " + e.getMessage());
        }

        System.err.println("==================");
    }
}

