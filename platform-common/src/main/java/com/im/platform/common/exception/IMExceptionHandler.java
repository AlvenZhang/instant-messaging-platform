package com.im.platform.common.exception;

import com.im.platform.common.response.ResponseMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获类
 * 统一处理应用中的异常，返回标准格式的错误响应
 */
@RestControllerAdvice
public class IMExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(IMExceptionHandler.class);

    /**
     * 处理IMException业务异常
     * @param e IMException异常
     * @return 错误响应
     */
    @ExceptionHandler(IMException.class)
    public Object handleIMException(IMException e) {
        logger.error("业务异常: code={}, message={}", e.getCode(), e.getMessage(), e);
        return ResponseMessageFactory.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理Exception系统异常
     * @param e Exception异常
     * @return 错误响应
     */
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        logger.error("系统异常: {}", e.getMessage(), e);
        return ResponseMessageFactory.error("500", "系统内部错误");
    }

    /**
     * 处理RuntimeException运行时异常
     * @param e RuntimeException异常
     * @return 错误响应
     */
    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException e) {
        logger.error("运行时异常: {}", e.getMessage(), e);
        return ResponseMessageFactory.error("500", "系统运行异常");
    }

    /**
     * 处理IllegalArgumentException参数异常
     * @param e IllegalArgumentException异常
     * @return 错误响应
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("参数异常: {}", e.getMessage(), e);
        return ResponseMessageFactory.error("400", "请求参数错误: " + e.getMessage());
    }

    /**
     * 处理NullPointerException空指针异常
     * @param e NullPointerException异常
     * @return 错误响应
     */
    @ExceptionHandler(NullPointerException.class)
    public Object handleNullPointerException(NullPointerException e) {
        logger.error("空指针异常: {}", e.getMessage(), e);
        return ResponseMessageFactory.error("500", "系统内部错误");
    }
}

