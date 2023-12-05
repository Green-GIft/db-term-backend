package com.apiserver.greengift._core.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BaseException {
    // 공통
    PERMISSION_DENIED_METHOD_ACCESS("사용할 수 없는 기능입니다.", 403),
    DATABASE_ERROR("데이터베이스 에러입니다.", 500),
    UNEXPECTED_EXCEPTION("예상치 못한 에러가 발생했습니다.", 500),
    INVALID_METHOD_ARGUMENTS("잘못된 메서드 또는 Argument입니다.", 500),

    // 회원
    USER_NOT_FOUND("서비스를 탈퇴했거나 가입하지 않은 유저의 요청입니다.", 404),
    USER_ROLE_WRONG("사용자 역할이 잘못 입력되었습니다.", 400),
    USER_EMAIL_NOT_FOUND("이메일을 찾을 수 없습니다.", 404),
    USER_UNAUTHORIZED("인증되지 않았습니다", 401),
    USER_PASSWORD_NOT_SAME("패스워드1과 패스워드2는 동일해야 합니다.",  400),
    USER_DUPLICATED_EMAIL("동일한 이메일이 존재합니다.", 400),
    USER_PASSWORD_WRONG("패스워드를 잘못 입력하셨습니다.", 400),

    FESTIVAL_DUPLICATED_NAME("이미 등록된 축제 이름입니다", 400),
    FESTIVAL_NOT_FOUND("존재하지 않는 축제입니다", 404),
    FESTIVAL_DATE_INVALID("축제 시작 날짜는 끝나는 날짜보다 늦어질 수 없습니다.", 400),
    FESTIVAL_COUNT_VALID("축제 참여 인원, 상품이 3개 이상이어야 가능합니다", 400),
    FESTIVAL_DUPLICATED_USER("이미 등록된 유저입니다.", 400),
    FESTIVAL_RANDOM_FINISHED("이미 추첨이 완료된 축제입니다.", 400),
    FESTIVAL_RESULT_NOT_FOUND("존재하지 않는 축제 결과입니다.", 404),
    FESTIVAL_CERTIFICATE_INVALID("이미 해당 축제는 인증되었습니다.", 400),

    PRODUCT_LIMIT_3("축제 당 상품은 3개까지 등록 가능합니다.", 400),
    PRODUCT_NOT_FOUND("존재하지 않는 상품입니다.", 404),
    PRODUCT_NO_AMOUNT("상품의 재고다 모두 소진되었습니다.", 400),
    PRODUCT_NO_MONEY("상품을 구매할 마일리지가 부족합니다.", 400),

    USER_FESTIVAL_NOT_FOUND("해당하는 유저 축제가 존재하지 않습니다.", 404),

    USER_PRODUCT_NOT_FOUND("해당하는 축제의 당첨 결과가 존재하지 않습니다.", 404),

    TRASH_NOT_FOUND("존재하지 않는 쓰레기입니다.", 404),
    TRASH_CATEGORY_WRONG("쓰레기 분류가 잘못되었습니다.", 400);

    @Getter
    private final String message;

    @Getter
    private final int status;
}