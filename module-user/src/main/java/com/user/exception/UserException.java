package com.user.exception;

import com.common.exception.BusinessException;

public class UserException extends BusinessException {
    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(UserErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
