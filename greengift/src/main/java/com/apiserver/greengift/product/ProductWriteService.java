package com.apiserver.greengift.product;

import com.apiserver.greengift._core.errors.BaseException;
import com.apiserver.greengift._core.errors.exception.BadRequestException;
import com.apiserver.greengift._core.errors.exception.ForbiddenException;
import com.apiserver.greengift._core.errors.exception.NotFoundException;
import com.apiserver.greengift._core.errors.exception.UnauthorizedException;
import com.apiserver.greengift.festival.Festival;
import com.apiserver.greengift.festival.FestivalJPARepository;
import com.apiserver.greengift.festival.FestivalRequest;
import com.apiserver.greengift.festival.user_festival.UserFestivalJPARepository;
import com.apiserver.greengift.product.constant.ProductCategory;
import com.apiserver.greengift.product.user_product.UserProduct;
import com.apiserver.greengift.product.user_product.UserProductJPARepository;
import com.apiserver.greengift.user.User;
import com.apiserver.greengift.user.participant.Participant;
import com.apiserver.greengift.user.participant.ParticipantJPARepository;
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
    private final ParticipantJPARepository participantJPARepository;

    public void addProduct(User user, FestivalRequest.AddProduct requestDTO, Long festivalId) {
        Festival festival = getFestivalById(festivalId);
        checkFestivalUserEqualToUser(festival, user);
        checkFestivalProductCount(festival);
        productJPARepository.save(requestDTO.toProduct(festival));
    }
    public void addAmount(User user, ProductRequest.AddProductAmount requestDTO, Long productId) {
        Product product = getProductById(productId);
        checkAddProductPermission(user, product);
        product.updateAmount(requestDTO.extraAmount());
    }
    public void buyProduct(User user, Long productId) {
        Product product = getProductById(productId);
        checkProductAmount(product);

        Participant participant = findParticipantById(user.getId());
        checkMileageValid(participant.getMileage(), product);
        participant.reduceMileage(product.getPrice());

        UserProduct userProduct = getUserProduct(product, participant);
        userProductJPARepository.save(userProduct);
    }

    private Participant findParticipantById(Long id) {
        return participantJPARepository.findById(id).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND)
        );
    }
    private UserProduct getUserProduct(Product product, User user) {
        return UserProduct.builder()
                .category(ProductCategory.MILEAGE)
                .product(product)
                .user(user)
                .build();
    }
    private Product getProductById(Long productId) {
        return productJPARepository.findById(productId).orElseThrow(
                () -> new NotFoundException(BaseException.PRODUCT_NOT_FOUND)
        );
    }
    private void checkMileageValid(Long mileage, Product product) {
        if (mileage < product.getPrice()){
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
