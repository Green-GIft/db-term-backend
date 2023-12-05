package com.apiserver.greengift.trash;

public class TrashResponse {

    public record FindTrashByUser(
            Long trashId,
            String date,
            String name,
            String category,
            String festivalName
    ){}

    public record FindBestTrashManager(
            String name,
            Long count
    ){}
}
