package com.apiserver.greengift.festival;

public class FestivalResponse {

    public record FindFestivalResult(
        String image,
        String name,
        String due_date
    ) {
    }

    public record FindJoinedFestival(
        String image,
        String start_date,
        String end_date,
        String name,
        String state
    ) {}

    public record FindFestivalByUser(
            String image,
            String start_date,
            String end_date,
            String name
    ) {}
}
