package com.im.platform.common.interceptor.impl;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * XSS漏洞检测工具类
 * 用于检测和防范XSS攻击
 */
public class XssUtils {

    // XSS攻击特征正则表达式
    private static final List<Pattern> XSS_PATTERNS = Arrays.asList(
            // Script标签
            Pattern.compile("<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("<script[^>]*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),

            // JavaScript事件处理器
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onerror(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onclick(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onmouseover(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onfocus(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onblur(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            // 其他危险标签
            Pattern.compile("<iframe[^>]*>.*?</iframe>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("<object[^>]*>.*?</object>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("<embed[^>]*>.*?</embed>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("<applet[^>]*>.*?</applet>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("<meta[^>]*>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<link[^>]*>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<style[^>]*>.*?</style>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            // 表达式和编码
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("@import", Pattern.CASE_INSENSITIVE),
            Pattern.compile("url\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    );

    // 需要特殊检查的敏感字符
    private static final String[] SENSITIVE_CHARS = {
            "<", ">", "\"", "'", "&", "(", ")", "{", "}", "[", "]",
            "javascript:", "vbscript:", "data:", "mocha:", "livescript:"
    };

    /**
     * 检查字符串是否包含XSS攻击特征
     * @param input 待检查的字符串
     * @return true表示包含XSS攻击特征，false表示安全
     */
    public static boolean checkXss(String input) {
        if (!StringUtils.hasText(input)) {
            return false;
        }

        // 转换为小写进行检查
        String lowerInput = input.toLowerCase();

        // 检查敏感字符
        for (String sensitiveChar : SENSITIVE_CHARS) {
            if (lowerInput.contains(sensitiveChar.toLowerCase())) {
                // 进一步检查是否真的是XSS攻击
                if (isSuspiciousPattern(lowerInput, sensitiveChar)) {
                    return true;
                }
            }
        }

        // 使用正则表达式检查XSS特征
        for (Pattern pattern : XSS_PATTERNS) {
            if (pattern.matcher(input).find()) {
                return true;
            }
        }

        // 检查HTML实体编码绕过
        return checkHtmlEntities(input);
    }

    /**
     * 检查是否是可疑模式
     * @param input 输入字符串（小写）
     * @param charPattern 字符模式
     * @return true表示可疑
     */
    private static boolean isSuspiciousPattern(String input, String charPattern) {
        // 检查是否是HTML标签
        if ("<".equals(charPattern) && input.contains(">")) {
            return true;
        }

        // 检查是否是脚本协议
        if (charPattern.contains(":")) {
            return true;
        }

        // 检查是否是事件处理器
        if (charPattern.equals("on") && input.contains("=")) {
            return true;
        }

        return false;
    }

    /**
     * 检查HTML实体编码绕过
     * @param input 输入字符串
     * @return true表示存在绕过攻击
     */
    private static boolean checkHtmlEntities(String input) {
        // 常见的HTML实体编码
        String[] entities = {
            "&lt;", "&gt;", "&quot;", "&apos;", "&amp;",
            "&#60;", "&#62;", "&#34;", "&#39;", "&#38;",
            "&x3c;", "&x3e;", "&x22;", "&x27;", "&x26;"
        };

        String decodedInput = input;
        for (String entity : entities) {
            // 如果包含HTML实体编码，进行解码后再检查
            if (decodedInput.contains(entity)) {
                String htmlDecoded = htmlDecode(decodedInput);
                if (!htmlDecoded.equals(decodedInput) && checkXss(htmlDecoded)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 简单的HTML解码
     * @param input 输入字符串
     * @return 解码后的字符串
     */
    private static String htmlDecode(String input) {
        return input.replace("&lt;", "<")
                   .replace("&gt;", ">")
                   .replace("&quot;", "\"")
                   .replace("&apos;", "'")
                   .replace("&amp;", "&")
                   .replace("&#60;", "<")
                   .replace("&#62;", ">")
                   .replace("&#34;", "\"")
                   .replace("&#39;", "'")
                   .replace("&#38;", "&")
                   .replace("&x3c;", "<")
                   .replace("&x3e;", ">")
                   .replace("&x22;", "\"")
                   .replace("&x27;", "'")
                   .replace("&x26;", "&");
    }

    /**
     * 检查HTTP请求中是否包含XSS攻击
     * @param request HTTP请求对象
     * @return true表示包含XSS攻击，false表示安全
     */
    public static boolean checkXss(HttpServletRequest request) {
        // 检查请求参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String[] values = entry.getValue();
            if (values != null) {
                for (String value : values) {
                    if (checkXss(value)) {
                        return true;
                    }
                }
            }
        }

        // 检查请求头
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> headerValues = request.getHeaders(headerName);
            while (headerValues.hasMoreElements()) {
                String headerValue = headerValues.nextElement();
                if (checkXss(headerValue)) {
                    return true;
                }
            }
        }

        // 检查请求体（如果是CacheHttpServletRequestWrapper）
        if (request instanceof com.im.platform.common.filter.CacheHttpServletRequestWrapper) {
            com.im.platform.common.filter.CacheHttpServletRequestWrapper cachedRequest =
                (com.im.platform.common.filter.CacheHttpServletRequestWrapper) request;
            String body = cachedRequest.getBodyAsString();
            if (checkXss(body)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 清理XSS攻击代码
     * @param input 输入字符串
     * @return 清理后的安全字符串
     */
    public static String cleanXss(String input) {
        if (!StringUtils.hasText(input)) {
            return input;
        }

        String cleaned = input;

        // 移除危险的HTML标签
        cleaned = cleaned.replaceAll("<script[^>]*>.*?</script>", "")
                        .replaceAll("<script[^>]*>", "")
                        .replaceAll("</script>", "")
                        .replaceAll("<iframe[^>]*>.*?</iframe>", "")
                        .replaceAll("<object[^>]*>.*?</object>", "")
                        .replaceAll("<embed[^>]*>.*?</embed>", "")
                        .replaceAll("<applet[^>]*>.*?</applet>", "")
                        .replaceAll("<meta[^>]*>", "")
                        .replaceAll("<link[^>]*>", "")
                        .replaceAll("<style[^>]*>.*?</style>", "");

        // 移除JavaScript协议和事件处理器
        cleaned = cleaned.replaceAll("(?i)javascript:", "")
                        .replaceAll("(?i)vbscript:", "")
                        .replaceAll("(?i)data:", "")
                        .replaceAll("(?i)on\\w+\\s*=", "")
                        .replaceAll("(?i)expression\\((.*?)\\)", "")
                        .replaceAll("(?i)eval\\((.*?)\\)", "")
                        .replaceAll("(?i)@import", "")
                        .replaceAll("(?i)url\\((.*?)\\)", "");

        return cleaned;
    }
}

