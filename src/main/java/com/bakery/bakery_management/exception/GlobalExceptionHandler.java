package com.bakery.bakery_management.exception;

import com.bakery.bakery_management.base.OperatorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Bắt lỗi nghiệp vụ do bạn tự ném ra
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public OperatorResult handleBusinessException(BusinessException ex) {
        // Trả về fail với message và code từ Exception
        return OperatorResult.fail(ex.getMessage());
    }

    // 2. Bắt các lỗi hệ thống không mong muốn (DB lỗi, NullPointer...)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public OperatorResult handleGeneralException(Exception ex) {
        return OperatorResult.fail("Hệ thống gặp sự cố: " + ex.getMessage());
    }
}
