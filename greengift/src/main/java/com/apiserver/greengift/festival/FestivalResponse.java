package com.apiserver.greengift.festival;

public class FestivalResponse {

    public record FindFestivalResult(
        String image,
        String name,
        String dueDate
    ) {
    }

    public record FindJoinedFestival(
        Long festivalId,
        String image,
        String startDate,
        String endDate,
        String name,
        String state
    ) {}

    public record FindFestivalByUser(
            Long festivalId,
            String image,
            String startDate,
            String endDate,
            String name
    ) {}
}
