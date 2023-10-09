package com.xiaolu.usercenter.exception;

import com.xiaolu.usercenter.common.BaseResponse;
import com.xiaolu.usercenter.common.ErrorCode;
import com.xiaolu.usercenter.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/10/3 10:06
 * @Description 全局异常处理器
 */
// AOP切面功能
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage() + "详情: " + e.getDescription(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeException(RuntimeException e) {
        log.error("runtimeException:  ", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }

}
