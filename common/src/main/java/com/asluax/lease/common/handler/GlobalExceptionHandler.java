package com.asluax.lease.common.handler;

import com.asluax.lease.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e) {
        Result<Object> result = Result.fail();
        result.setMessage(e.getMessage());
        return result;
    }
}