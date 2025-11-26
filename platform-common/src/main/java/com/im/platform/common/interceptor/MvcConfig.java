package com.im.platform.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类
 * 实现WebMvcConfigurer接口，用于注册和配置拦截器
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private IMInterceptor imInterceptor;

    /**
     * 添加拦截器到Spring MVC配置中
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(imInterceptor)
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns(
                        "/login",           // 登录接口
                        "/register",        // 注册接口
                        "/logout",          // 登出接口
                        "/error",           // 错误页面
                        "/actuator/**",     // 监控端点
                        "/swagger-ui/**",   // Swagger UI
                        "/swagger-resources/**", // Swagger资源
                        "/v2/api-docs",     // API文档
                        "/v3/api-docs",     // API文档
                        "/webjars/**",      // WebJars资源
                        "/static/**",       // 静态资源
                        "/public/**",       // 公共资源
                        "/css/**",          // CSS文件
                        "/js/**",           // JavaScript文件
                        "/images/**",       // 图片文件
                        "/favicon.ico",     // 网站图标
                        "/**/*.html",       // HTML文件
                        "/**/*.css",        // CSS文件
                        "/**/*.js",         // JavaScript文件
                        "/**/*.png",        // PNG图片
                        "/**/*.jpg",        // JPG图片
                        "/**/*.jpeg",       // JPEG图片
                        "/**/*.gif",        // GIF图片
                        "/**/*.svg"         // SVG图片
                )
                .order(1); // 设置拦截器顺序
    }
}

