package com.apiserver.greengift._core.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BaseException {
    // 공통
    PERMISSION_DENIED_METHOD_ACCESS("사용할 수 없는 기능입니다.", 403),
    DATABASE_ERROR("데이터베이스 에러입니다.", 500),
    UNEXPECTED_EXCEPTION("예상치 못한 에러가 발생했습니다.", 500),

    // 회원
    USER_NOT_FOUND("서비스를 탈퇴했거나 가입하지 않은 유저의 요청입니다.", 404),
    USER_ROLE_WRONG("사용자 역할이 잘못 입력되었습니다.", 400),
    USER_EMAIL_NOT_FOUND("이메일을 찾을 수 없습니다.", 404),
    USER_UNAUTHORIZED("인증되지 않았습니다", 401),
    USER_PASSWORD_NOT_SAME("패스워드1과 패스워드2는 동일해야 합니다.",  400),
    USER_EMAIL_EXIST("동일한 이메일이 존재합니다.", 400),
    USER_PASSWORD_WRONG("패스워드를 잘못 입력하셨습니다.", 400)

    ;

    @Getter
    private final String message;

    @Getter
    private final int status;
}