package com.apiserver.greengift.product.user_product;

import com.apiserver.greengift.product.constant.ProductCategory;
import com.apiserver.greengift.product.Product;
import com.apiserver.greengift.festival.user_festival.UserFestival;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_product_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserFestival userFestival;

    @Column(name = "product_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Builder
    public UserProduct(Long id, Product product, UserFestival userFestival, ProductCategory category){
        this.id = id;
        this.product = product;
        this.userFestival = userFestival;
        this.category = category;
    }

}

