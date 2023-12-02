package com.apiserver.greengift.festival.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductCategory {
    FESTIVAL("축제"),
    MILEAGE("마일리지")
    ;
    @Getter
    private final String productCategoryName;
}
