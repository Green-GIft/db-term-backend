package com.apiserver.greengift.festival;

import com.apiserver.greengift._core.errors.BaseException;
import com.apiserver.greengift._core.errors.exception.BadRequestException;
import com.apiserver.greengift._core.errors.exception.NotFoundException;
import com.apiserver.greengift.festival.constant.FestivalStatus;
import com.apiserver.greengift.festival.user_festival.UserFestival;
import com.apiserver.greengift.festival.user_festival.UserFestivalJPARepository;
import com.apiserver.greengift.product.Product;
import com.apiserver.greengift.product.ProductJPARepository;
import com.apiserver.greengift.product.constant.ProductCategory;
import com.apiserver.greengift.product.user_product.UserProduct;
import com.apiserver.greengift.product.user_product.UserProductJDBCRepository;
import com.apiserver.greengift.user.User;
import com.apiserver.greengift.user.UserJPARepository;
import com.apiserver.greengift.user.festival_manager.FestivalManager;
import com.apiserver.greengift.user.festival_manager.FestivalManagerJPARepository;
import com.apiserver.greengift.user.participant.Participant;
import com.apiserver.greengift.user.participant.ParticipantJPARepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FestivalWriteService {

    private final ParticipantJPARepository participantJPARepository;
    private final FestivalManagerJPARepository festivalManagerJPARepository;
    private final FestivalJPARepository festivalJPARepository;
    private final ProductJPARepository productJPARepository;
    private final UserFestivalJPARepository userFestivalJPARepository;
    private final UserProductJDBCRepository userProductJDBCRepository;
    private final UserJPARepository userJPARepository;

    public void addFestival(User user, FestivalRequest.AddFestival requestDTO) {
        FestivalManager festivalManager = findFestivalManagerByUserId(user.getId());
        LocalDate start_date = LocalDate.parse(requestDTO.startDate());
        LocalDate end_date = LocalDate.parse(requestDTO.endDate());

        checkValidName(requestDTO.name());
        checkValidDate(start_date, end_date);
        festivalJPARepository.save(requestDTO.toFestival(festivalManager, start_date, end_date));
    }
    public void joinFestival(User user, FestivalRequest.JoinFestival requestDTO) {
        Festival festival = getFestivalByName(requestDTO.name());
        checkFestivalFinished(festival);

        User user1 = findParticipantByUserId(user.getId());
        checkDuplicatedFestival(user1, festival);

        UserFestival userFestival = getUserFestival(festival, user1);
        userFestivalJPARepository.save(userFestival);
    }
    public void findRandomResult(User user, Long festivalId) {
        Festival festival = getFestivalById(festivalId);
        checkFestivalFinished(festival);

        // festivalId인 UserFestival 중 회수 완료된 상태인 사람 중 3명 랜덤으로 select
        List<UserFestival> userFestivalList = userFestivalJPARepository.findUserFestivalByRandom(festival);
        List<Product> productList = productJPARepository.findProductLimit(festival);
        checkUserProductCountValid(userFestivalList, productList);

        // 당첨된 유저들은 당첨 상품인 userProduct 추가해줌
        addUserProduct(userFestivalList, productList);

        // 당첨되지 않은 유저들은 마일리지 10점 추가해줌
        updateMileage(festival, userFestivalList);
    }

    private void checkFestivalFinished(Festival festival) {
        if (festival.getIsFinished()){
            throw new BadRequestException(BaseException.FESTIVAL_RANDOM_FINISHED);
        }
    }
    private void updateMileage(Festival festival, List<UserFestival> userFestivalList){
        List<UserFestival> nonSelectedList = userFestivalJPARepository.findUserByFestival(festival, userFestivalList);
        List<Long> nonSelectedParticipantList = nonSelectedList.stream().map(it->it.getUser().getId()).toList();
        participantJPARepository.updateMileage(nonSelectedParticipantList);
        userFestivalJPARepository.updateFailStatus(nonSelectedList);
        festival.finished();
    }
    private void addUserProduct(List<UserFestival> userFestivalList, List<Product> productList){
        List<UserProduct> userProductList = new ArrayList<>();
        for (int i=0; i<3; i++){
            User user = userFestivalList.get(i).getUser();
            Product product = productList.get(i);
            UserProduct userProduct = getUserProduct(product, user);
            userProductList.add(userProduct);
        }
        userFestivalJPARepository.updateSuccessStatus(userFestivalList);
        userProductJDBCRepository.batchInsertUserProduct(userProductList);
    }
    private void checkDuplicatedFestival(User user, Festival festival) {
        List<UserFestival> festivalList = userFestivalJPARepository.findParticipantAndFestival(user, festival);
        if (!festivalList.isEmpty()){
            throw new BadRequestException(BaseException.FESTIVAL_DUPLICATED_USER);
        }
    }
    private void checkUserProductCountValid(List<UserFestival> userFestivalList, List<Product> productList) {
        if (!(productList.size() >= 3 && userFestivalList.size() >= 3)){
            throw new BadRequestException(BaseException.FESTIVAL_COUNT_VALID);
        }
    }
    private void checkValidDate(LocalDate start_date, LocalDate end_date) {
        if (start_date.isAfter(end_date)){
            throw new BadRequestException(BaseException.FESTIVAL_DATE_INVALID);
        }
    }
    private UserProduct getUserProduct(Product product, User user){
        return UserProduct.builder()
                .product(product)
                .user(user)
                .category(ProductCategory.FESTIVAL)
                .build();
    }
    private User findParticipantByUserId(Long userId) {
        return userJPARepository.findById(userId).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND)
        );
    }
    private UserFestival getUserFestival(Festival festival, User user){
        return UserFestival.builder()
                .festival(festival)
                .user(user)
                .status(FestivalStatus.WAITING)
                .build();
    }
    private Festival getFestivalByName(String name){
        return festivalJPARepository.findByName(name).orElseThrow(
                () -> new NotFoundException(BaseException.FESTIVAL_NOT_FOUND)
        );
    }
    private Festival getFestivalById(Long festivalId) {
        return festivalJPARepository.findById(festivalId).orElseThrow(
                () -> new NotFoundException(BaseException.FESTIVAL_NOT_FOUND)
        );
    }
    private FestivalManager findFestivalManagerByUserId(Long userId) {
        return festivalManagerJPARepository.findById(userId).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND)
        );
    }
    private void checkValidName(String name) {
        Optional<Festival> festivalOptional = festivalJPARepository.findByName(name);
        if (festivalOptional.isPresent()) {
            throw new BadRequestException(BaseException.FESTIVAL_DUPLICATED_NAME);
        }
    }
}
