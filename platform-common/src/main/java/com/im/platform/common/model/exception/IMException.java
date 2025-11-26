package com.im.platform.common.model.exception;

/**
 * IM平台自定义异常类
 * 继承RuntimeException，用于业务异常处理
 */
public class IMException extends RuntimeException {

    private String code;
    private String message;

    public IMException() {
        super();
    }

    public IMException(String message) {
        super(message);
        this.message = message;
    }

    public IMException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public IMException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public IMException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public IMException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "IMException{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

