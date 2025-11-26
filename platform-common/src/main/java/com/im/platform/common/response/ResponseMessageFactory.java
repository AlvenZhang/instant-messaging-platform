package com.im.platform.common.response;

/**
 * 响应数据工厂类
 * 提供静态方法用于创建标准格式的响应数据实体类
 */
public class ResponseMessageFactory {

    /**
     * 成功响应（无数据）
     * @return 成功响应消息
     */
    public static <T> ResponseMessage<T> success() {
        return new ResponseMessage<>("200", "操作成功");
    }

    /**
     * 成功响应（带数据）
     * @param data 响应数据
     * @return 成功响应消息
     */
    public static <T> ResponseMessage<T> success(T data) {
        return new ResponseMessage<>("200", "操作成功", data);
    }

    /**
     * 成功响应（自定义消息，无数据）
     * @param message 自定义消息
     * @return 成功响应消息
     */
    public static <T> ResponseMessage<T> success(String message) {
        return new ResponseMessage<>("200", message);
    }

    /**
     * 成功响应（自定义消息，带数据）
     * @param message 自定义消息
     * @param data 响应数据
     * @return 成功响应消息
     */
    public static <T> ResponseMessage<T> success(String message, T data) {
        return new ResponseMessage<>("200", message, data);
    }

    /**
     * 失败响应（默认错误信息）
     * @return 失败响应消息
     */
    public static <T> ResponseMessage<T> error() {
        return new ResponseMessage<>("500", "操作失败");
    }

    /**
     * 失败响应（自定义错误码和错误信息）
     * @param code 错误码
     * @param message 错误信息
     * @return 失败响应消息
     */
    public static <T> ResponseMessage<T> error(String code, String message) {
        return new ResponseMessage<>(code, message);
    }

    /**
     * 失败响应（自定义错误信息，使用默认错误码500）
     * @param message 错误信息
     * @return 失败响应消息
     */
    public static <T> ResponseMessage<T> error(String message) {
        return new ResponseMessage<>("500", message);
    }

    /**
     * 根据布尔值返回成功或失败响应
     * @param success 操作是否成功
     * @return 响应消息
     */
    public static <T> ResponseMessage<T> result(boolean success) {
        return success ? success() : error();
    }

    /**
     * 根据布尔值返回成功或失败响应（带自定义消息）
     * @param success 操作是否成功
     * @param successMessage 成功消息
     * @param errorMessage 失败消息
     * @return 响应消息
     */
    public static <T> ResponseMessage<T> result(boolean success, String successMessage, String errorMessage) {
        return success ? success(successMessage) : error(errorMessage);
    }

    /**
     * 根据布尔值返回成功或失败响应（成功时带数据）
     * @param success 操作是否成功
     * @param data 成功时的数据
     * @return 响应消息
     */
    public static <T> ResponseMessage<T> result(boolean success, T data) {
        return success ? success(data) : error();
    }

    /**
     * 根据布尔值返回成功或失败响应（带自定义消息和数据）
     * @param success 操作是否成功
     * @param successMessage 成功消息
     * @param errorMessage 失败消息
     * @param data 成功时的数据
     * @return 响应消息
     */
    public static <T> ResponseMessage<T> result(boolean success, String successMessage, String errorMessage, T data) {
        return success ? success(successMessage, data) : error(errorMessage);
    }

    /**
     * 参数校验失败响应
     * @param message 校验失败信息
     * @return 失败响应消息
     */
    public static <T> ResponseMessage<T> validationError(String message) {
        return new ResponseMessage<>("400", "参数校验失败: " + message);
    }

    /**
     * 未授权响应
     * @param message 未授权信息
     * @return 失败响应消息
     */
    public static <T> ResponseMessage<T> unauthorized(String message) {
        return new ResponseMessage<>("401", message != null ? message : "未授权访问");
    }

    /**
     * 禁止访问响应
     * @param message 禁止访问信息
     * @return 失败响应消息
     */
    public static <T> ResponseMessage<T> forbidden(String message) {
        return new ResponseMessage<>("403", message != null ? message : "禁止访问");
    }

    /**
     * 资源不存在响应
     * @param message 资源不存在信息
     * @return 失败响应消息
     */
    public static <T> ResponseMessage<T> notFound(String message) {
        return new ResponseMessage<>("404", message != null ? message : "资源不存在");
    }
}

