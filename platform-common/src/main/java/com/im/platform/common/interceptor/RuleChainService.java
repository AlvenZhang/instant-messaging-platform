package com.im.platform.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器链服务接口
 * 定义拦截器链服务的通用行为
 */
public interface RuleChainService {

    /**
     * 执行拦截器链服务
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @return true表示继续执行下一个拦截器或请求处理，false表示中断处理
     * @throws Exception 执行过程中可能抛出的异常
     */
    boolean execute(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 获取当前拦截器服务的优先级
     * 数值越小，优先级越高
     * @return 优先级数值
     */
    int getOrder();
}

