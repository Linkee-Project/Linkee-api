package com.linkee.linkeeapi.common.exception;

import com.linkee.linkeeapi.common.model.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*컨트롤러에서 BusinessException이 발생하면 이 메서드가 호출됨.

e.getErrorCode()로 예외에 담긴 ErrorCode를 가져옴.

ApiResponse.failure를 통해 표준 API 응답 구조로 변환:*/
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e){
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatusCode());
    }
}
