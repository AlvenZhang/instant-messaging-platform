package com.im.platform.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.im.common.domain.jwt.JwtUtils;
import com.im.platform.common.session.SessionContext;
import com.im.platform.common.session.UserSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 拦截器链基础抽象类
 * 提供通用的工具方法供子类使用
 */
public abstract class BaseRuleChainService implements RuleChainService {


    @Value("${jwt.secret}")
    private String jwtSecret;
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
            String accessToken = request.getHeader("accessToken");
            if (!JwtUtils.checkSign(accessToken, jwtSecret)){
                return null;
            }
            String info = JwtUtils.getInfo(accessToken);
            if (StringUtils.hasText(info)){
                return null;
            }
            return JSON.parseObject(info, UserSession.class);

        } catch (Exception e) {
            // 记录异常日志
            System.err.println("获取用户session时发生异常: " + e.getMessage());
            return null;
        }
    }

}

