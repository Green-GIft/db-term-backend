package com.apiserver.greengift.festival.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FestivalStatus {
    SUCCESS("추첨 성공"),
    FAIL("추첨 실패"),
    READY("회수 완료"),
    WAITING("회수 대기")
    ;
    @Getter
    private final String festivalStatusName;
}
