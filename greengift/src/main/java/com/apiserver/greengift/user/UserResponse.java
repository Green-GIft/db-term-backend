package com.apiserver.greengift.user;

public class UserResponse {
    public record FindGrade(
        String grade,
        Long mileage
    ) {}
}
