package com.apiserver.greengift.festival;

import com.apiserver.greengift.festival.user_festival.UserFestival;
import com.apiserver.greengift.festival.user_festival.UserFestivalJPARepository;
import com.apiserver.greengift.gift.user_product.UserProduct;
import com.apiserver.greengift.gift.user_product.UserProductJPARepository;
import com.apiserver.greengift.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FestivalReadService {
    private final UserFestivalJPARepository userFestivalJPARepository;

    private final UserProductJPARepository userProductJPARepository;
    private final FestivalJPARepository festivalJPARepository;

    public FestivalResponse.FindFestivalResult findFestivalResult(User user, Long festivalId) {
        Optional<UserProduct> userProduct = userProductJPARepository.findByUserIdAndFestivalId(user.getId(), festivalId);
        return userProduct.map(product -> new FestivalResponse.FindFestivalResult(
                product.getProduct().getImage(),
                product.getProduct().getName(),
                product.getProduct().getDueDate().toString()
        )).orElse(null);
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
}
