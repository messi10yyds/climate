package com.basic.climate.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获所有未处理的异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        e.printStackTrace(); // 在控制台打印，方便调试
        return Result.fail(e.getMessage());
    }
}
