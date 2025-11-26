package com.im.platform.common.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 缓存参数过滤器
 * 实现Filter接口，用于将HTTP请求体缓存起来，解决请求体只能读取一次的问题
 */
@Component
@WebFilter(filterName = "cacheFilter", urlPatterns = "/*")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CacheFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化过滤器时执行的操作
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 只处理HTTP请求
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;

            // 判断是否需要缓存请求体
            if (shouldCacheRequestBody(request)) {
                // 使用缓存包装器包装原始请求
                CacheHttpServletRequestWrapper cachedRequest = new CacheHttpServletRequestWrapper(request);
                // 将包装后的请求传递给下一个过滤器
                filterChain.doFilter(cachedRequest, servletResponse);
            } else {
                // 不需要缓存时直接传递原始请求
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            // 非HTTP请求直接传递
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * 判断是否需要缓存请求体
     * @param request HTTP请求对象
     * @return true表示需要缓存，false表示不需要缓存
     */
    private boolean shouldCacheRequestBody(HttpServletRequest request) {
        String contentType = request.getContentType();
        String method = request.getMethod();

        // 只对POST、PUT、PATCH请求进行缓存
        if (!"POST".equalsIgnoreCase(method) &&
            !"PUT".equalsIgnoreCase(method) &&
            !"PATCH".equalsIgnoreCase(method)) {
            return false;
        }

        // 只对JSON、XML、表单等包含请求体的内容类型进行缓存
        if (contentType == null) {
            return false;
        }

        return contentType.contains("application/json") ||
               contentType.contains("application/xml") ||
               contentType.contains("text/xml") ||
               contentType.contains("application/x-www-form-urlencoded") ||
               contentType.contains("multipart/form-data");
    }

    @Override
    public void destroy() {
        // 销毁过滤器时执行的操作
        Filter.super.destroy();
    }
}

