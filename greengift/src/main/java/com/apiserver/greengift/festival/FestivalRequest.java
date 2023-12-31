package com.apiserver.greengift.festival;

import com.apiserver.greengift.product.Product;
import com.apiserver.greengift.user.festival_manager.FestivalManager;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class FestivalRequest {

    public record AddFestival (
            @NotEmpty(message = "축제 이름은 비어있으면 안됩니다.")
            @Size(max = 100, message = "축제 이름은 100자 이내여야 합니다.")
            String name,

            @NotEmpty(message = "축제 이름은 비어있으면 안됩니다.")
            @Size(max = 20, message = "축제 시작하는 날짜는 20자 이내여야 합니다.")
            @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])", message = "시작하는 날짜의 포맷팅을 맞춰주세요.")
            String startDate,

            @NotEmpty(message = "축제 이름은 비어있으면 안됩니다.")
            @Size(max = 20, message = "축제 끝나는 날짜는 20자 이내여야 합니다.")
            @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])", message = "끝나는 날짜의 포맷팅을 맞춰주세요.")
            String endDate,

            @NotEmpty(message = "이미지는 비어있으면 안됩니다.")
            String image
    ) {
        public Festival toFestival(FestivalManager festivalManager, LocalDate startDate, LocalDate endDate){
            return Festival.builder()
                    .festivalManager(festivalManager)
                    .name(name)
                    .startDate(startDate)
                    .endDate(endDate)
                    .image(image)
                    .isFinished(false)
                    .build();
        }
    }

    public record JoinFestival (
            @NotEmpty(message = "축제 이름은 비어있으면 안됩니다.")
            @Size(max = 100, message = "축제 이름은 100자 이내여야 합니다.")
            String name
    ){
    }

    public record AddProduct(

            @NotEmpty(message = "이름은 비어있으면 안됩니다.")
            @Size(max = 20, message = "이름은 20자 이내여야 합니다.")
            String name,

            @NotEmpty(message = "회사는 비어있으면 안됩니다.")
            @Size(max = 20, message = "회사는 20자 이내여야 합니다.")
            String company,

            @NotNull(message = "랭크는 비어있으면 안됩니다.")
            @Min(value=1, message="랭크는 1 이상이어야 합니다.")
            Long rank,

            @Size(max = 20, message = "만료기한은 20자 이내여야 합니다.")
            @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])", message = "끝나는 날짜의 포맷팅을 맞춰주세요.")
            String dueDate,

            @NotNull(message = "추가 수량은 비어있으면 안됩니다.")
            @Min(value = 1, message = "추가 수량은 1개 이상이어야 합니다.")
            @Max(value=100000000, message = "추가 수량은 1억 이하입니다.")
            Long extraAmount,

            @NotEmpty(message = "이미지는 비어있으면 안됩니다.")
            String image,

            @NotNull(message = "가격은 비어있으면 안됩니다.")
            @Min(value=1, message="가격은 1원 이상이어야 합니다.")
            @Max(value=100000000, message = "가격은 1억 이하입니다.")
            Long price
    ){
        public Product toProduct(Festival festival){
            return Product.builder()
                    .festival(festival)
                    .name(name)
                    .dueDate(LocalDate.parse(dueDate))
                    .extraAmount(extraAmount)
                    .image(image)
                    .company(company)
                    .rank(rank)
                    .price(price)
                    .build();
        }
    }
}
