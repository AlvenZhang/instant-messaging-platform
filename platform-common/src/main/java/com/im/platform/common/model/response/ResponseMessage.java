package com.im.platform.common.model.response;

/**
 * 统一响应数据实体类
 * 用于封装API响应数据，包含状态码、消息和数据
 * @param <T> 泛型，表示响应数据的类型
 */
public class ResponseMessage<T> {

    private String code;
    private String message;
    private T data;

    public ResponseMessage() {
    }

    public ResponseMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseMessage(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 判断响应是否成功
     * @return 成功返回true，失败返回false
     */
    public boolean isSuccess() {
        return "200".equals(code) || "0".equals(code);
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

