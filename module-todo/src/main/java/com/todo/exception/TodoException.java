package com.todo.exception;

import com.common.exception.BusinessException;

public class TodoException extends BusinessException {
    public TodoException(TodoErrorCode errorCode) {
        super(errorCode);
    }

    public TodoException(TodoErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
