package com.xiaolu.usercenter.exception;

import com.xiaolu.usercenter.common.ErrorCode;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/10/2 20:06
 * @Description 自定义异常类
 */
public class BusinessException extends RuntimeException{

    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
