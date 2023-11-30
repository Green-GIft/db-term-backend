package com.apiserver.greengift.user.constant;

import com.apiserver.greengift._core.errors.BaseException;
import com.apiserver.greengift._core.errors.exception.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum Role {
    PARTICIPANT("participant"),
    TRASH_MANAGER("trash_manager"),
    FESTIVAL_MANAGER("festival_manager");

    @Getter
    private final String roleName;

    public static Role valueOfRole(String role) {
        return Arrays.stream(values())
                .filter(value -> value.roleName.equals(role))
                .findAny()
                .orElseThrow(() -> new BadRequestException(BaseException.USER_ROLE_WRONG));
    }
}
