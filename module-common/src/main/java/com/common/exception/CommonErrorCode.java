package com.common.exception;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode{
    UNEXCEPTED_ERROR("0001", "未預期錯誤，請稍後重試", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND("0002", "資料內容為空", HttpStatus.NOT_FOUND),
    EMPTY_RESPONSE("0003", "服務回應內容為空", HttpStatus.BAD_GATEWAY);

    private String code;
    private String message;
    private HttpStatus httpStatus;

    CommonErrorCode(String code, String message, HttpStatus httpStatus) {
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
