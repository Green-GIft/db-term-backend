package com.apiserver.greengift.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Grade {
    SEED("씨앗"),
    SPROUT("새싹"),
    TREE("나무"),
    FLOWER("꽃"),
    FRUIT("열매"),
    ;
    @Getter
    private final String gradeName;
}
