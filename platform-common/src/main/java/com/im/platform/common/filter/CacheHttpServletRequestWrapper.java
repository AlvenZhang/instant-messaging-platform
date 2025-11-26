package com.im.platform.common.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * HTTP请求参数缓存包装类
 * 继承HttpServletRequestWrapper，用于缓存请求体内容，解决HTTP请求体只能读取一次的问题
 */
public class CacheHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] requestBody;
    private final HttpServletRequest request;

    /**
     * 构造函数
     * @param request 原始HTTP请求对象
     * @throws IOException 读取请求体时可能抛出IO异常
     */
    public CacheHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.request = request;
        this.requestBody = cacheRequestBody(request);
    }

    /**
     * 缓存请求体内容
     * @param request HTTP请求对象
     * @return 请求体字节数组
     * @throws IOException 读取请求体时可能抛出IO异常
     */
    private byte[] cacheRequestBody(HttpServletRequest request) throws IOException {
        if (request.getContentLength() <= 0) {
            return new byte[0];
        }

        try (ServletInputStream inputStream = request.getInputStream()) {
            byte[] buffer = new byte[request.getContentLength()];
            int bytesRead = 0;
            int totalRead = 0;

            while (totalRead < buffer.length && (bytesRead = inputStream.read(buffer, totalRead, buffer.length - totalRead)) != -1) {
                totalRead += bytesRead;
            }

            if (totalRead < buffer.length) {
                byte[] result = new byte[totalRead];
                System.arraycopy(buffer, 0, result, 0, totalRead);
                return result;
            }

            return buffer;
        }
    }

    /**
     * 获取缓存的请求体内容
     * @return 请求体字节数组
     */
    public byte[] getRequestBody() {
        return requestBody;
    }

    /**
     * 获取原始请求对象
     * @return 原始HTTP请求对象
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    /**
     * 获取请求体内容字符串
     * @return 请求体内容字符串
     */
    public String getBodyAsString() {
        return new String(requestBody, StandardCharsets.UTF_8);
    }
}

