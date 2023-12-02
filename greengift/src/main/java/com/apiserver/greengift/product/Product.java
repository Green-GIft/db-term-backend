package com.apiserver.greengift.product;

import com.apiserver.greengift.festival.Festival;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "product_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Festival festival;

    @Column(nullable = false)
    private String name;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "extra_amount", nullable = false)
    private Long extraAmount;

    @Lob
    private String image;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private Long rank;

    @Column(nullable = false)
    private Long price;

    @Builder
    public Product(Long id, Festival festival, String name, LocalDate dueDate,
                   Long extraAmount, String image, String company, Long rank, Long price){
        this.id = id;
        this.festival = festival;
        this.name = name;
        this.dueDate = dueDate;
        this.extraAmount = extraAmount;
        this.image = image;
        this.company = company;
        this.rank = rank;
        this.price = price;
    }

    public void updateAmount(Long extraAmount) {
        this.extraAmount = this.extraAmount + extraAmount;
    }
}
