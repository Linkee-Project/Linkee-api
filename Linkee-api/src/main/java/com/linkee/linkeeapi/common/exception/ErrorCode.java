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
    UNAUTHORIZED_ACCESS("1000", "관리자만 접근 가능합니다", HttpStatus.UNAUTHORIZED),
    INVALID_REQUEST("1001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    INVALID_USER_ID("1002", "해당 사용자가 없습니다", HttpStatus.BAD_REQUEST),
    INVALID_ADMIN_ID("1003", "잘못된 관리자 ID입니다.", HttpStatus.BAD_REQUEST),
    INVALID_INCORRECT_FORMAT("1004","잘못된 형식입니다.", HttpStatus.BAD_REQUEST),

    //공지사항 관련 오류(2000번대 사용)
    NOTICE_NOT_FOUND("2000", "해당 공지사항이 없습니다", HttpStatus.NOT_FOUND),
    INVALID_NOTICE_ID("2001", "잘못된 공지사항 ID입니다.", HttpStatus.BAD_REQUEST),
    DATABASE_ERROR("2002", "데이터베이스 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    //문의 관련 오류 (3000번대 사용)
    INQUIRY_NOT_FOUND("3000", "해당 문의를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_INQUIRY_ID("3001", "잘못된 문의 ID입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_ANSWERED("3003", "이미 답변이 등록된 문의입니다.", HttpStatus.BAD_REQUEST),

    //신고 관련 오류(4000번대 사용)
    REPORT_NOT_FOUND("4000", "해당 신고를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    REPORT_NO_ACCESS("4001", "조회 권한이 없거나 존재하지 않는 신고입니다.", HttpStatus.FORBIDDEN),

    //문제 관련 오류(5000번대 사용)
    QUESTION_NOT_FOUND("5000","해당 문제를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    QUESTION_UPDATE_NOT_ALLOWED("5001", "검증된 문제는 수정할 수 없습니다.", HttpStatus.CONFLICT),
    QUESTION_DELETE_NOT_ALLOWED("5002", "검증된 문제는 삭제할 수 없습니다.", HttpStatus.CONFLICT),
    FORBIDDEN_QUESTION_ACCESS("5003", "문제에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_ANSWER_INDEX("5004", "정답 인덱스가 옵션과 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    QUESTION_ALREADY_QUALIFIED("5005","이미 검증된 문제입니다.",HttpStatus.CONFLICT),

    //카데고리 관련 오류(6000번대 사용)
    CATEGORY_NOT_FOUND("6000","해당 카테고리를 찾을 수 없습니다.",HttpStatus.NOT_FOUND),

    //관계 관련 오류(7000번대 사용)
    RELATION_ALREADY_EXISTS("7000", "이미 친구 관계입니다.", HttpStatus.CONFLICT),
    RELATION_REQUEST_ALREADY_EXISTS("7001", "이미 친구 요청을 보냈습니다.", HttpStatus.CONFLICT),

    //북마크
    BOOKMARK_ALREADY_EXISTS("8000","이미 북마크한 문제 입니다.", HttpStatus.CONFLICT),

    //퀴즈룸 관련 오류(9000번대 사용)
    QUIZ_ROOM_NOT_FOUND("9000", "퀴즈방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    QUIZ_ROOM_NOT_WAITING("9001", "대기 중인 방만 시작할 수 있습니다.", HttpStatus.BAD_REQUEST),
    QUIZ_ROOM_NOT_READY("9002", "모든 참가자가 준비 완료 상태여야 합니다.", HttpStatus.BAD_REQUEST),
    QUIZ_ROOM_GAME_IN_PLAY("9003", "게임 진행 중에는 방을 나갈 수 없습니다.", HttpStatus.BAD_REQUEST),
    QUIZ_ROOM_UNAUTHORIZED("9004", "방장만 시작할 수 있습니다.", HttpStatus.FORBIDDEN),
    QUIZ_INDEX_NOT_FOUND("9005", "퀴즈 인덱스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    QUIZ_ROOM_NOT_IN_PLAY("9006", "진행 중인 게임에 대해서만 요청할 수 있습니다.", HttpStatus.BAD_REQUEST),
    QUIZ_ROOM_NOT_IN_WAITING_STATE("9007", "대기 중인 방에만 참여할 수 있습니다.", HttpStatus.BAD_REQUEST),
    QUIZ_ROOM_FULL("9008", "방이 가득 찼습니다.", HttpStatus.BAD_REQUEST),
    USER_ALREADY_IN_ROOM("9009", "이미 참여한 방입니다.", HttpStatus.BAD_REQUEST),

    //댓글 관련 오류(10000번대 사용)
    COMMENT_NOT_FOUND("10000", "존재하지 않는 댓글입니다.", HttpStatus.NOT_FOUND),
    COMMENT_PARENT_NOT_FOUND("10001", "부모 댓글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    COMMENT_FORBIDDEN_ACCESS("10002", "댓글 작성자만 수행할 수 있습니다.", HttpStatus.FORBIDDEN),
    CHAT_ROOM_NOT_FOUND("11111","방이존재하지 않습니다" ,HttpStatus.NOT_FOUND );

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