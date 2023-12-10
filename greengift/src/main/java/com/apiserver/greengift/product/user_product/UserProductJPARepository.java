package com.apiserver.greengift.product.user_product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserProductJPARepository extends JpaRepository<UserProduct, Long> {

    @Query("select u from UserProduct u " +
            "join fetch u.product p1 " +
            "join fetch p1.festival f "+
            "join fetch u.user p2 " +
            "where f.id = :festivalId and p2.id = :userId and u.category = 'FESTIVAL'")
    Optional<UserProduct> findByUserIdAndFestivalId(@Param("userId") Long userId, @Param("festivalId") Long festivalId);

    @Query("select u from UserProduct u join fetch u.user p " +
            "where p.id = :userId ")
    List<UserProduct> findByUserId(Long userId);
}
