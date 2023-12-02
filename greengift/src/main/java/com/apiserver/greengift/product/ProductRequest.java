package com.apiserver.greengift.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ProductRequest {

    public record AddProductAmount(
        @NotNull(message = "추가 수량은 비어있으면 안됩니다.")
        @Min(value = 1, message = "추가 수량은 1개 이상이어야 합니다.")
        Long extra_amount
    ){}
}
