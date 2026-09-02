package com.common.exception;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Order(-1) // 要比 spring.mvc.problemdetails.enabled 建立的 @RestControllerAdvice (order = 1) 還晚執行
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected @Nullable ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        String traceId = MDC.get("traceId");

        if (ex instanceof BusinessException businessException) {
            log.warn("[Business Exception] traceId={}, errorCode={}, message={}", traceId, businessException.getErrorCode().getCode(), ex.getMessage());
        } else {
            log.warn("[Request Rejected] traceId={}, type={}, message={}", traceId, ex.getClass().getSimpleName(), ex.getMessage());
        }

        ResponseEntity<Object> response = super.handleExceptionInternal(ex, body, headers, statusCode, request);
        if (response != null && response.getBody() instanceof ProblemDetail problemDetail && traceId != null) {
            problemDetail.setProperty("traceId", traceId);
        }
        return response;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(Exception ex, HttpServletRequest request) {
        String traceId = MDC.get("traceId");
        log.error("[Unexpected Exception] traceId={}, uri={}", traceId, request.getRequestURI(), ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "未預期錯誤，請稍後重試");
        problemDetail.setProperty("errorCode", "0001");
        problemDetail.setProperty("traceId", traceId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}
