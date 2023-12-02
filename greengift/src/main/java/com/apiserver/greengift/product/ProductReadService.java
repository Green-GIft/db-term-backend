package com.apiserver.greengift.product;

import com.apiserver.greengift.product.user_product.UserProduct;
import com.apiserver.greengift.product.user_product.UserProductJPARepository;
import com.apiserver.greengift.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductReadService {

    private final ProductJPARepository productJPARepository;
    private final UserProductJPARepository userProductJPARepository;

    public List<ProductResponse.FindProductAll> findAllProduct(User user) {
        List<Product> productList = productJPARepository.findAllProduct();
        return productList.stream()
                .map(it ->
                        new ProductResponse.FindProductAll(
                                it.getId(),
                                it.getImage(),
                                it.getName(),
                                it.getCompany(),
                                it.getPrice()
                        )
                ).toList();
    }
    public List<ProductResponse.FindProductByParticipant> findProductByParticipant(User user) {
        List<UserProduct> userProductList = userProductJPARepository.findByUserId(user.getId());
        return userProductList.stream()
                .map(it ->
                        new ProductResponse.FindProductByParticipant(
                                it.getProduct().getImage(),
                                it.getProduct().getName(),
                                it.getProduct().getCompany(),
                                it.getCategory().getProductCategoryName()
                        )
                ).toList();
    }
    public List<ProductResponse.FindProductByFestivalManager> findProductByFestivalManager(User user) {
        List<Product> productList = productJPARepository.findAllByFestivalManager(user.getId());
        return productList.stream()
                .map(it ->
                        new ProductResponse.FindProductByFestivalManager(
                                it.getId(),
                                it.getImage(),
                                it.getName(),
                                it.getCompany(),
                                it.getPrice()
                        )
                ).toList();
    }
}
