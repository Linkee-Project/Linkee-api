package com.linkee.linkeeapi.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ErrorCode {
    //사용법 아래에 적어놓았습니다

    //1000번대는 공통 오류 처리 (가져다 쓰세요!)
    //관리자 접근 불가 관련 오류
    UNAUTHORIZED_ACCESS("1000", "관리자만 접근 가능합니다", HttpStatus.UNAUTHORIZED),
    INVALID_REQUEST("1001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    INVALID_USER_ID("1002", "해당 사용자가 없습니다", HttpStatus.BAD_REQUEST),
    INVALID_ADMIN_ID("1003", "잘못된 관리자 ID입니다.", HttpStatus.BAD_REQUEST),

    //공지사항 관련 오류(2000번대 사용)
    NOTICE_NOT_FOUND("2000", "해당 공지사항이 없습니다", HttpStatus.NOT_FOUND),
    INVALID_NOTICE_ID("2001", "잘못된 공지사항 ID입니다.", HttpStatus.BAD_REQUEST),
    DATABASE_ERROR("2002", "데이터베이스 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),


    //문의 관련 오류 (3000번대 사용)
    INQUIRY_NOT_FOUND("3000", "해당 문의를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_INQUIRY_ID("3001", "잘못된 문의 ID입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_ANSWERED("3003", "이미 답변이 등록된 문의입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatusCode httpStatusCode;

    /* 사용법
    * errorContent : entity명_오류_내용
    * errorCode: 다른 엔터티와 다른 자신의 고유 번호대 사용 (1000번 부터 시작)
    * message: "~다"로 끝나도록
    * HttpStatusCode: http오류코드 찾아서 넣어주세요 (404, 500 등등,,처리를 연결하기 위함)
    *
    * 자기가 생각하는 오류가 발생하는 곳에
    * .orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
    * throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS);
    * 이런식으로 오류를 던지면 됩니다
    *
    * 1000번대 공통 오류 처리는
    * throw new BusinessException(ErrorCode.INVALID_REQUEST, "문의 제목은 필수 입력값입니다.");
    * 이런식으로 message를 덮어씌울 수 있습니다
    *
    * postman에
    * {
        "success": false,
        "data": null,
        "errorCode": "1001",
        "message": "관리자만 접근 가능합니다",
        "timestamp": "2025-11-03T14:30:56.9244768"
    * }
    * 위와 비슷한 메세지가 출력되는지 확인!
    * */
}
