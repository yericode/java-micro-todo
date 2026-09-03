package com.todo.exception;

import com.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum TodoErrorCode implements ErrorCode {

    INVALID_CONTENT("1001", "輸入內容有誤", HttpStatus.BAD_REQUEST),
    INVALID_CONTENT_LENGTH("1002", "輸入內容超過長度限制", HttpStatus.CONTENT_TOO_LARGE);

    private String code;
    private String message;
    private HttpStatus httpStatus;

    TodoErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
