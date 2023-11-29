package com.greengift.apiserver._core.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BaseException {
    ;

    @Getter
    private final String message;

    @Getter
    private final int code;

    @Getter
    private final int status;
}