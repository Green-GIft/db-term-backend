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

    static public Grade getNextGrade(Grade grade){
        if (grade == SEED) return SPROUT;
        else if (grade == SPROUT) return TREE;
        else if (grade == TREE) return FLOWER;
        else if (grade == FLOWER) return FRUIT;
        else return FRUIT;
    }
}
