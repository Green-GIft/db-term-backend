package com.apiserver.greengift.festival.user_product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProductJPARepository extends JpaRepository<UserProduct, Long> {

    @Query("select u from UserProduct u join fetch u.userFestival uf " +
            "join fetch u.product " +
            "join fetch uf.participant p " +
            "join fetch uf.festival uff " +
            "where uff.id = :festivalId and p.id = :userId")
    Optional<UserProduct> findByUserIdAndFestivalId(@Param("userId") Long userId, @Param("festivalId") Long festivalId);

}