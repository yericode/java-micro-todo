package com.user.exception;

import com.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCode {

    NOT_EXISTS("2001", "查無使用者", HttpStatus.NOT_FOUND),
    INVALID_INPUT("2002", "必要參數有誤", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_OR_PASSWORD("2003", "帳號或密碼錯誤", HttpStatus.BAD_REQUEST),
    DUPLICATE_EMAIL("2004", "已有重複內容", HttpStatus.BAD_REQUEST);

    private String code;
    private String message;
    private HttpStatus httpStatus;

    UserErrorCode(String code, String message, HttpStatus httpStatus) {
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
