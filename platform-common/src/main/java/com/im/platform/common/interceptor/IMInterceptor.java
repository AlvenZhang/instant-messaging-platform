package com.im.platform.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通用拦截器实现类
 * 继承BaseInterceptor，实现具体的拦截逻辑，依次执行已排序的拦截器链服务
 */
@Component
public class IMInterceptor extends BaseInterceptor {

    // 不需要拦截的路径
    private static final String[] EXCLUDE_PATHS = {
        "/login",
        "/register",
        "/logout",
        "/error",
        "/actuator/",
        "/swagger-",
        "/v2/api-docs",
        "/webjars/",
        "/static/",
        "/favicon.ico"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查是否需要拦截当前请求
        if (!shouldIntercept(request, EXCLUDE_PATHS)) {
            return true;
        }

        // 获取所有已排序的拦截器链服务
        List<RuleChainService> services = getRuleChainServices();

        // 依次执行拦截器链服务
        for (RuleChainService service : services) {
            try {
                // 如果任何一个拦截器服务返回false，则中断处理
                if (!service.execute(request, response)) {
                    return false;
                }
            } catch (Exception e) {
                // 记录异常日志
                System.err.println("拦截器链服务执行异常: " + e.getMessage());
                e.printStackTrace();

                // 根据异常类型决定是否继续执行
                if (shouldContinueOnError(e, request, handler)) {
                    continue;
                } else {
                    setErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器内部错误");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 判断异常发生时是否应该继续执行下一个拦截器服务
     * @param e 发生的异常
     * @param request HTTP请求对象
     * @param handler 处理器对象
     * @return true表示继续执行，false表示中断处理
     */
    private boolean shouldContinueOnError(Exception e, HttpServletRequest request, Object handler) {
        // 如果是业务异常且处理的是控制器方法，可以选择继续执行
        if (e instanceof RuntimeException && handler instanceof HandlerMethod) {
            // TODO: 可以根据具体业务需求调整异常处理策略
            return false;
        }

        // 其他异常类型默认中断处理
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理资源或执行一些收尾操作
        try {
            // TODO: 可以在这里添加日志记录、统计信息等
            if (ex != null) {
                System.err.println("请求处理完成时发生异常: " + ex.getMessage());
            }
        } finally {
            // 调用父类方法
            super.afterCompletion(request, response, handler, ex);
        }
    }
}

