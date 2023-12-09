package com.apiserver.greengift.product.user_product;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserProductJDBCRepository {
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    @Autowired
    public UserProductJDBCRepository(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    public void batchInsertUserProduct(List<UserProduct> userProductList){
        String TABLE_NAME = "user_product_tb";
        String sql = String.format("""
                INSERT INTO %s (product_category, product_id, user_festival_id)
                VALUES (?, ?, ?)
                """, TABLE_NAME);

        entityManager.flush();
        entityManager.clear();

        jdbcTemplate.batchUpdate(sql, userProductList, userProductList.size(),
                (ps, userProduct) -> {
                    ps.setString(1, userProduct.getCategory().name());
                    ps.setLong(2, userProduct.getProduct().getId());
                    ps.setLong(3, userProduct.getUserFestival().getId());
                });
    }
}
