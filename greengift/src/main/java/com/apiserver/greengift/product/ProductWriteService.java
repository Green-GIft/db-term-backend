package com.apiserver.greengift.product;

import com.apiserver.greengift._core.errors.BaseException;
import com.apiserver.greengift._core.errors.exception.BadRequestException;
import com.apiserver.greengift._core.errors.exception.ForbiddenException;
import com.apiserver.greengift._core.errors.exception.NotFoundException;
import com.apiserver.greengift._core.errors.exception.UnauthorizedException;
import com.apiserver.greengift.festival.Festival;
import com.apiserver.greengift.festival.FestivalJPARepository;
import com.apiserver.greengift.festival.FestivalRequest;
import com.apiserver.greengift.festival.user_festival.UserFestival;
import com.apiserver.greengift.festival.user_festival.UserFestivalJPARepository;
import com.apiserver.greengift.product.constant.ProductCategory;
import com.apiserver.greengift.product.user_product.UserProduct;
import com.apiserver.greengift.product.user_product.UserProductJPARepository;
import com.apiserver.greengift.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductWriteService {

    private final FestivalJPARepository festivalJPARepository;
    private final ProductJPARepository productJPARepository;
    private final UserProductJPARepository userProductJPARepository;
    private final UserFestivalJPARepository userFestivalJPARepository;

    public void addProduct(User user, FestivalRequest.AddProduct requestDTO, Long festivalId) {
        Festival festival = getFestivalById(festivalId);
        checkFestivalUserEqualToUser(festival, user);
        checkFestivalProductCount(festival);
        productJPARepository.save(requestDTO.toProduct(festival));
    }
    public void addAmount(User user, ProductRequest.AddProductAmount requestDTO, Long productId) {
        Product product = getProductById(productId);
        checkAddProductPermission(user, product);
        product.updateAmount(requestDTO.extra_amount());
    }
    public void buyProduct(User user, Long productId) {
        Product product = getProductById(productId);
        checkProductAmount(product);

        UserFestival userFestival = getUserFestivalByUserIdAndProductId(product, user.getId());
        checkMileageValid(userFestival, product);

        UserProduct userProduct = getUserProduct(userFestival, product);
        userProductJPARepository.save(userProduct);
    }
    private UserFestival getUserFestivalByUserIdAndProductId(Product product, Long userId) {
        Long festivalId = product.getFestival().getId();
        return userFestivalJPARepository.getUserFestivalByUserIdAndProductId(userId, festivalId).orElseThrow(
                () -> new NotFoundException(BaseException.USER_FESTIVAL_NOT_FOUND)
        );
    }
    private UserProduct getUserProduct(UserFestival userFestival, Product product) {
        return UserProduct.builder()
                .category(ProductCategory.MILEAGE)
                .product(product)
                .userFestival(userFestival)
                .build();
    }
    private Product getProductById(Long productId) {
        return productJPARepository.findById(productId).orElseThrow(
                () -> new NotFoundException(BaseException.PRODUCT_NOT_FOUND)
        );
    }
    private void checkMileageValid(UserFestival userFestival, Product product) {
        if (userFestival.getParticipant().getMileage() < product.getPrice()){
            throw new BadRequestException(BaseException.PRODUCT_NO_MONEY);
        }
    }
    private void checkProductAmount(Product product) {
        if (product.getExtraAmount() <= 0){
            throw new BadRequestException(BaseException.PRODUCT_NO_AMOUNT);
        }
    }
    private void checkAddProductPermission(User user, Product product) {
        if (!Objects.equals(user.getId(), product.getFestival().getFestivalManager().getId())){
            throw new ForbiddenException(BaseException.PERMISSION_DENIED_METHOD_ACCESS);
        }
    }
    private void checkFestivalProductCount(Festival festival){
        Long count = productJPARepository.findProductCount(festival);
        if (count >= 3){
            throw new BadRequestException(BaseException.PRODUCT_LIMIT_3);
        }
    }
    private Festival getFestivalById(Long festivalId) {
        return festivalJPARepository.findById(festivalId).orElseThrow(
                () -> new NotFoundException(BaseException.FESTIVAL_NOT_FOUND)
        );
    }
    private void checkFestivalUserEqualToUser(Festival festival, User user) {
        if (!Objects.equals(festival.getFestivalManager().getId(), user.getId())){
            throw new UnauthorizedException(BaseException.PERMISSION_DENIED_METHOD_ACCESS);
        }
    }
}
