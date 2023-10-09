package com.xiaolu.usercenter.utils;

import com.xiaolu.usercenter.common.BaseResponse;
import com.xiaolu.usercenter.common.ErrorCode;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/10/2 14:00
 * @Description 返回封装好BaseResponse对象的工具类
 */
public class ResultUtils {
    /**
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok", "");
    }

    /**
     * 失败
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static  BaseResponse error(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }

    /**
     * 失败
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode, message, description);
    }

    /**
     * 失败
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String description) {
        return new BaseResponse<>(errorCode, description);
    }
}
