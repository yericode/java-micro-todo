package com.common.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class BusinessException extends ErrorResponseException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage());
    }

    public BusinessException(ErrorCode errorCode, String detail) {
        super(errorCode.getHttpStatus(), buildProblemDetail(errorCode, detail), null);
        this.errorCode = errorCode;
    }

    private static ProblemDetail buildProblemDetail(ErrorCode errorCode, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(errorCode.getHttpStatus(), detail);
        problemDetail.setProperty("errorCode", errorCode.getCode());
        return problemDetail;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
