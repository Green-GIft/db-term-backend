package com.apiserver.greengift.festival;

public class FestivalResponse {

    public record FindFestivalResult(
        String image,
        String name,
        String due_date
    ) {
    }

    public record FindJoinedFestival(
        Long festivalId,
        String image,
        String start_date,
        String end_date,
        String name,
        String state
    ) {}

    public record FindFestivalByUser(
            Long festivalId,
            String image,
            String start_date,
            String end_date,
            String name
    ) {}
}
