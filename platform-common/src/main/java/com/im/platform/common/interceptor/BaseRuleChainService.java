package com.im.platform.common.interceptor;

import com.im.platform.common.session.SessionContext;
import com.im.platform.common.session.UserSession;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 拦截器链基础抽象类
 * 提供通用的工具方法供子类使用
 */
public abstract class BaseRuleChainService implements RuleChainService {

    /**
     * 获取当前请求的IP地址
     * @param request HTTP请求对象
     * @return IP地址字符串，如果无法获取则返回"unknown"
     */
    protected String getIp(HttpServletRequest request) {
        try {
            // 尝试从各种可能的头部获取真实IP
            String ip = request.getHeader("X-Forwarded-For");
            if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个IP值，第一个为真实IP
                int index = ip.indexOf(',');
                if (index != -1) {
                    return ip.substring(0, index).trim();
                } else {
                    return ip.trim();
                }
            }

            ip = request.getHeader("Proxy-Client-IP");
            if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip.trim();
            }

            ip = request.getHeader("WL-Proxy-Client-IP");
            if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip.trim();
            }

            ip = request.getHeader("HTTP_CLIENT_IP");
            if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip.trim();
            }

            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip.trim();
            }

            // 如果都获取不到，则使用request.getRemoteAddr()
            ip = request.getRemoteAddr();
            return StringUtils.hasText(ip) ? ip : "unknown";

        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * 获取当前请求的session
     * @param request HTTP请求对象
     * @return UserSession对象，如果session不存在或验证失败则返回null
     */
    protected UserSession getUserSession(HttpServletRequest request) {
        try {
            // 首先尝试从SessionContext中获取
            UserSession session = SessionContext.getSession();
            if (session != null) {
                return session;
            }

            // 从请求属性中获取
            Object sessionObj = request.getAttribute("userSession");
            if (sessionObj instanceof UserSession) {
                return (UserSession) sessionObj;
            }

            // TODO: 实现token验证逻辑
            // 从请求头中获取token
            String token = getTokenFromRequest(request);
            if (StringUtils.hasText(token)) {
                // TODO: 验证token并从token中获取用户信息
                // 这里需要根据具体的token验证逻辑实现
                // UserSession userSession = tokenService.validateToken(token);
                // if (userSession != null) {
                //     SessionContext.setSession(userSession);
                //     return userSession;
                // }
            }

            return null;

        } catch (Exception e) {
            // 记录异常日志
            System.err.println("获取用户session时发生异常: " + e.getMessage());
            return null;
        }
    }

    /**
     * 从请求中获取token
     * @param request HTTP请求对象
     * @return token字符串，如果不存在则返回null
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        try {
            // 从Authorization头中获取token
            String authHeader = request.getHeader("Authorization");
            if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }

            // 从参数中获取token
            String tokenParam = request.getParameter("token");
            if (StringUtils.hasText(tokenParam)) {
                return tokenParam;
            }

            // 从请求属性中获取token
            Object tokenAttr = request.getAttribute("token");
            if (tokenAttr != null) {
                return tokenAttr.toString();
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断当前用户是否已登录
     * @param request HTTP请求对象
     * @return true表示已登录，false表示未登录
     */
    protected boolean isUserLoggedIn(HttpServletRequest request) {
        return getUserSession(request) != null;
    }

    /**
     * 获取当前用户名
     * @param request HTTP请求对象
     * @return 用户名，如果未登录则返回null
     */
    protected String getCurrentUserName(HttpServletRequest request) {
        UserSession session = getUserSession(request);
        return session != null ? session.getUserName() : null;
    }

    /**
     * 获取当前用户昵称
     * @param request HTTP请求对象
     * @return 用户昵称，如果未登录则返回null
     */
    protected String getCurrentNickName(HttpServletRequest request) {
        UserSession session = getUserSession(request);
        return session != null ? session.getNickName() : null;
    }
}

