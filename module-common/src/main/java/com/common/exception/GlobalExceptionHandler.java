package com.common.exception;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
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

        if (body instanceof ProblemDetail problemDetail && traceId != null) {
            problemDetail.setProperty("traceId", traceId);
        }
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }
}
