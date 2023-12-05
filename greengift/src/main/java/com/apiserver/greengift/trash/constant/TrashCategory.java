package com.apiserver.greengift.trash.constant;

import com.apiserver.greengift._core.errors.BaseException;
import com.apiserver.greengift._core.errors.exception.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum TrashCategory {
    PAPER("종이"),
    PET("페트병"),
    CAN("캔, 고철류"),
    PLASTIC("플라스틱"),
    GLASS("유리"),
    VINYL("비닐"),
    GENERAL("일반 쓰레기"),
    ETC("기타")
    ;

    @Getter
    private final String categoryName;

    public static TrashCategory valueOfCategory(String category) {
        return Arrays.stream(values())
                .filter(value -> value.categoryName.equals(category))
                .findAny()
                .orElseThrow(() -> new BadRequestException(BaseException.TRASH_CATEGORY_WRONG));
    }
}
