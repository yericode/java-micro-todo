package com.common.exception;

/**
 * 通用業務邏輯異常實現類
 */
public class CommonException extends BusinessException {
    public CommonException(CommonErrorCode errorCode) {
        super(errorCode);
    }

    public CommonException(CommonErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
