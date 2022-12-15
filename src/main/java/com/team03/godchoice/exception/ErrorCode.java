package com.team03.godchoice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //에러코드 쓰는곳

    //관리자 오류
    USER_ERROR(HttpStatus.BAD_REQUEST.value(), "A001", "관리자 계정이 아닙니다"),

    //유저 오류
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND.value(), "M001", "유저가 존재하지 않습니다."),
    DELETED_USER_EXCEPTION(HttpStatus.BAD_REQUEST.value(), "M002", "이미 탈퇴한 계정입니다.\n다른 계정으로 시도해 주세요."),

    //게시물 오류
    NOT_FOUND_POST(HttpStatus.NOT_FOUND.value(), "P001", "게시물이 존재하지 않습니다."),
    DATE_STATUS_ERROR(HttpStatus.BAD_REQUEST.value(), "P002", "날짜 입력이 잘못되었습니다."),

    //권한 오류
    NO_PERMISSION_DELETE(HttpStatus.BAD_REQUEST.value(), "PE001", "삭제할 권한이 없습니다."),
    NO_PERMISSION_CHANGE(HttpStatus.BAD_REQUEST.value(), "PE002", "수정할 권한이 없습니다"),

    //이미지 오류
    NOT_FOUND_IMG(HttpStatus.NOT_FOUND.value(), "I001", "이미지가 존재하지 않습니다."),

    //댓글 오류
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND.value(), "C001", "댓글이 존재하지 않습니다."),
    COMMENT_ERROR(HttpStatus.BAD_REQUEST.value(), "C002", "대댓글의 댓글의 포스트 아이디가 다릅니다."),

    //알림 오류
    NOT_FOUND_NOTICE(HttpStatus.NOT_FOUND.value(), "N001", "알림이 존재하지 않습니다."),

    //알수없는 오류
    ERROR(HttpStatus.NO_CONTENT.value(), "E001", "알수업는오류"),
    ERROR_LOGIN(HttpStatus.BAD_REQUEST.value(), "E002", "로그인을 다시 시도해주세요"),

    //토큰오류
    TOKEN_ERROR(HttpStatus.FORBIDDEN.value(), "T001","토큰이 유효하지 않습니다.");


    private final int status;
    private final String code;
    private final String message;
}
