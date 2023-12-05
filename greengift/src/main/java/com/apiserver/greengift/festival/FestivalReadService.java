package com.apiserver.greengift.festival;

import com.apiserver.greengift._core.errors.BaseException;
import com.apiserver.greengift._core.errors.exception.BadRequestException;
import com.apiserver.greengift._core.errors.exception.NotFoundException;
import com.apiserver.greengift.festival.constant.FestivalStatus;
import com.apiserver.greengift.festival.user_festival.UserFestival;
import com.apiserver.greengift.festival.user_festival.UserFestivalJPARepository;
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
public class FestivalReadService {
    private final UserFestivalJPARepository userFestivalJPARepository;
    private final UserProductJPARepository userProductJPARepository;
    private final FestivalJPARepository festivalJPARepository;

    public FestivalResponse.FindFestivalResult findFestivalResult(User user, Long festivalId) {
        UserFestival userFestival = getUserFestival(user.getId(), festivalId);
        if (userFestival.getStatus() == FestivalStatus.FAIL) {
            return null;
        }
        else if (userFestival.getStatus() == FestivalStatus.SUCCESS){
            UserProduct userProduct = getUserProduct(user.getId(), festivalId);
            return getFindFestivalResult(userProduct);
        }
        else throw new BadRequestException(BaseException.FESTIVAL_RESULT_NOT_FOUND);
    }

    public List<FestivalResponse.FindJoinedFestival> findJoinedFestival(User user) {
        List<UserFestival> userFestivalList = userFestivalJPARepository.findByParticipantId(user.getId());
        return userFestivalList.stream()
                .map(it ->
                        new FestivalResponse.FindJoinedFestival(
                                it.getFestival().getId(),
                                it.getFestival().getImage(),
                                it.getFestival().getStartDate().toString(),
                                it.getFestival().getEndDate().toString(),
                                it.getFestival().getName(),
                                it.getStatus().getFestivalStatusName()
                        )
                ).toList();
    }

    public List<FestivalResponse.FindFestivalByUser> findFestivalByUser(User user) {
        List<Festival> festivalList = festivalJPARepository.findByFestivalManagerId(user.getId());
        return festivalList.stream()
                .map( it ->
                        new FestivalResponse.FindFestivalByUser(
                                it.getId(),
                                it.getImage(),
                                it.getStartDate().toString(),
                                it.getEndDate().toString(),
                                it.getName()
                        )
                ).toList();
    }

    private static FestivalResponse.FindFestivalResult getFindFestivalResult(UserProduct userProduct) {
        return new FestivalResponse.FindFestivalResult(
                userProduct.getProduct().getImage(),
                userProduct.getProduct().getName(),
                userProduct.getProduct().getDueDate().toString()
        );
    }
    private UserProduct getUserProduct(Long userId, Long festivalId){
        return userProductJPARepository.findByUserIdAndFestivalId(userId, festivalId).orElseThrow(
                () ->  new NotFoundException(BaseException.USER_PRODUCT_NOT_FOUND)
        );
    }
    private UserFestival getUserFestival(Long userId, Long festivalId) {
        return userFestivalJPARepository.findUserFestivalByUserIdAndProductId(userId, festivalId).orElseThrow(
                () -> new NotFoundException(BaseException.USER_FESTIVAL_NOT_FOUND)
        );
    }
}
