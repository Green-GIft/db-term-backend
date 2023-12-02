package com.apiserver.greengift.product;

import jakarta.validation.constraints.NotNull;

public class ProductRequest {

    public record AddProductAmount(
        @NotNull(message = "추가 수량은 비어있으면 안됩니다.")
        Long extra_amount
    ){}
}
