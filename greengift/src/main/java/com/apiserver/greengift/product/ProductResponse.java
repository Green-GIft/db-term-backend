package com.apiserver.greengift.product;

public class ProductResponse {

    public record FindProductAll(
        Long productId,
        String image,
        String name,
        String company,
        Long price
    ){}

    public record FindProductByParticipant(
        String image,
        String name,
        String company,
        String category
    ){}

    public record FindProductByFestivalManager(
        Long productId,
        String image,
        String name,
        String company,
        Long price
    ){}

}
