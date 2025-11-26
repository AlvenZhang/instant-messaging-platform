package com.im.platform.common.model.session;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Session上下文类
 * 用于从当前请求上下文中获取用户Session信息
 */
public class SessionContext {

    /**
     * 从上下文中获取当前请求的session
     * @return UserSession 当前用户会话信息
     */
    public static UserSession getSession() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                Object sessionObj = request.getAttribute("userSession");
                if (sessionObj instanceof UserSession) {
                    return (UserSession) sessionObj;
                }
            }
        } catch (Exception e) {
            // 无法获取上下文时返回null
        }
        return null;
    }

    /**
     * 设置当前请求的session
     * @param userSession 用户会话信息
     */
    public static void setSession(UserSession userSession) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                request.setAttribute("userSession", userSession);
            }
        } catch (Exception e) {
            // 无法获取上下文时忽略
        }
    }

    /**
     * 获取当前用户名
     * @return 用户名，如果session不存在返回null
     */
    public static String getCurrentUserName() {
        UserSession session = getSession();
        return session != null ? session.getUserName() : null;
    }

    /**
     * 获取当前用户昵称
     * @return 用户昵称，如果session不存在返回null
     */
    public static String getCurrentNickName() {
        UserSession session = getSession();
        return session != null ? session.getNickName() : null;
    }

    /**
     * 清除当前请求的session
     */
    public static void clearSession() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                request.removeAttribute("userSession");
            }
        } catch (Exception e) {
            // 无法获取上下文时忽略
        }
    }
}

