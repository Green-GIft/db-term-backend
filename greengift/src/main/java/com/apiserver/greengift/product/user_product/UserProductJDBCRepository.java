package com.apiserver.greengift.product.user_product;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserProductJDBCRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserProductJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsertUserProduct(List<UserProduct> userProductList){
        String TABLE_NAME = "user_product_tb";
        String sql = String.format("""
                INSERT INTO %s (product_category, product_id, user_festival_id)
                VALUES (?, ?, ?)
                """, TABLE_NAME);

        jdbcTemplate.batchUpdate(sql, userProductList, userProductList.size(),
                (ps, userProduct) -> {
                    ps.setString(1, userProduct.getCategory().name());
                    ps.setLong(2, userProduct.getProduct().getId());
                    ps.setLong(3, userProduct.getUserFestival().getId());
                });
    }
}
