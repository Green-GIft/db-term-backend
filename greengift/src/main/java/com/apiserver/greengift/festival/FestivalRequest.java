package com.apiserver.greengift.festival;

import com.apiserver.greengift.gift.Product;
import com.apiserver.greengift.user.festival_manager.FestivalManager;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class FestivalRequest {

    public record AddFestival (
            @NotEmpty(message = "축제 이름은 비어있으면 안됩니다.")
            @Size(max = 100, message = "축제 이름은 100자 이내여야 합니다.")
            String name,

            @NotEmpty(message = "축제 이름은 비어있으면 안됩니다.")
            @Size(max = 20, message = "축제 시작하는 날짜는 20자 이내여야 합니다.")
            @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])", message = "시작하는 날짜의 포맷팅을 맞춰주세요.")
            String start_date,

            @NotEmpty(message = "축제 이름은 비어있으면 안됩니다.")
            @Size(max = 20, message = "축제 끝나는 날짜는 20자 이내여야 합니다.")
            @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])", message = "끝나는 날짜의 포맷팅을 맞춰주세요.")
            String end_date,

            @NotEmpty(message = "이미지는 비어있으면 안됩니다.")
            String image
    ) {
        public Festival toFestival(FestivalManager festivalManager, LocalDate start_date, LocalDate end_date){
            return Festival.builder()
                    .festivalManager(festivalManager)
                    .name(name)
                    .startDate(start_date)
                    .endDate(end_date)
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
            Long rank,

            @Size(max = 20, message = "만료기한은 20자 이내여야 합니다.")
            @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])", message = "끝나는 날짜의 포맷팅을 맞춰주세요.")
            String due_date,

            @NotNull(message = "추가 수량은 비어있으면 안됩니다.")
            Long extra_amount,

            @NotEmpty(message = "이미지는 비어있으면 안됩니다.")
            String image,

            @NotNull(message = "가격은 비어있으면 안됩니다.")
            Long price
    ){
        public Product toProduct(Festival festival){
            return Product.builder()
                    .festival(festival)
                    .name(name)
                    .dueDate(LocalDate.parse(due_date))
                    .extraAmount(extra_amount)
                    .image(image)
                    .company(company)
                    .rank(rank)
                    .price(price)
                    .build();
        }
    }
}
