package com.xiaolu.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/10/2 13:24
 * @Description 通用返回类
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 4533795319073769863L;

    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

    public BaseResponse(ErrorCode errorCode, String message, String description) {
        this(errorCode.getCode(), null, message, description);
    }

    public BaseResponse(ErrorCode errorCode, String description) {
        this(errorCode.getCode(), null, errorCode.getMessage(), description);
    }
}
