package com.linkee.linkeeapi.common.exception;

import lombok.Getter;

/* 코드 어디선가에서 문제가 발생하면 throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND) 처럼 던짐*/
@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // ✅ 추가된 생성자 (직접 커스텀 메시지 전달 가능)
    public BusinessException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}
