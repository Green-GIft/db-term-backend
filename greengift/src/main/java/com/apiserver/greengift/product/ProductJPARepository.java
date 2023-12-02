package com.apiserver.greengift.product;

import com.apiserver.greengift.festival.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductJPARepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.festival = :festival order by p.rank asc limit 3")
    List<Product> findProductLimit(@Param("festival") Festival festival);

    @Query("select count(*) from Product p where p.festival = :festival")
    Long findProductCount(@Param("festival") Festival festival);
}
