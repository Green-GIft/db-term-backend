package com.apiserver.greengift.festival;

import com.apiserver.greengift._core.errors.BaseException;
import com.apiserver.greengift._core.errors.exception.BadRequestException;
import com.apiserver.greengift._core.errors.exception.NotFoundException;
import com.apiserver.greengift._core.errors.exception.UnauthorizedException;
import com.apiserver.greengift.festival.constant.FestivalStatus;
import com.apiserver.greengift.gift.constant.ProductCategory;
import com.apiserver.greengift.gift.Product;
import com.apiserver.greengift.gift.ProductJPARepository;
import com.apiserver.greengift.festival.user_festival.UserFestival;
import com.apiserver.greengift.festival.user_festival.UserFestivalJPARepository;
import com.apiserver.greengift.gift.user_product.UserProduct;
import com.apiserver.greengift.gift.user_product.UserProductJPARepository;
import com.apiserver.greengift.user.User;
import com.apiserver.greengift.user.festival_manager.FestivalManager;
import com.apiserver.greengift.user.festival_manager.FestivalManagerJPARepository;
import com.apiserver.greengift.user.participant.Participant;
import com.apiserver.greengift.user.participant.ParticipantJPARepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final UserProductJPARepository userProductRepository;

    public void addFestival(User user, FestivalRequest.AddFestival requestDTO) {
        FestivalManager festivalManager = findFestivalManagerByUserId(user.getId());
        LocalDate start_date = LocalDate.parse(requestDTO.start_date());
        LocalDate end_date = LocalDate.parse(requestDTO.end_date());

        checkValidName(requestDTO.name());
        checkValidDate(start_date, end_date);
        festivalJPARepository.save(requestDTO.toFestival(festivalManager, start_date, end_date));
    }
    public void addProduct(User user, FestivalRequest.AddProduct requestDTO, Long festivalId) {
        Festival festival = getFestivalById(festivalId);
        checkFestivalUserEqualToUser(festival, user);
        checkFestivalProductCount(festival);
        productJPARepository.save(requestDTO.toProduct(festival));
    }
    public void joinFestival(User user, FestivalRequest.JoinFestival requestDTO) {
        Festival festival = getFestivalByName(requestDTO.name());
        Participant participant = findParticipantByUserId(user.getId());
        checkDuplicatedFestival(participant, festival);
        UserFestival userFestival = getUserFestival(festival, participant);
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
        List<Long> nonSelectedParticipantList = nonSelectedList.stream().map(it->it.getParticipant().getId()).collect(Collectors.toList());
        List<Long> nonSelectedUserFestivalList = nonSelectedList.stream().map(UserFestival::getId).toList();
        participantJPARepository.updateMileage(nonSelectedParticipantList);
        userFestivalJPARepository.updateFailStatus(nonSelectedUserFestivalList);
        festival.finished();
    }
    private void addUserProduct(List<UserFestival> userFestivalList, List<Product> productList){
        for (int i=0; i<3; i++){
            UserFestival userFestival = userFestivalList.get(i);
            Product product = productList.get(i);
            UserProduct userProduct = getUserProduct(userFestival, product);
            userProductRepository.save(userProduct);
            userFestival.updateStatus(FestivalStatus.SUCCESS);
        }
    }
    private void checkDuplicatedFestival(Participant participant, Festival festival) {
        List<UserFestival> festivalList = userFestivalJPARepository.findParticipantAndFestival(participant, festival);
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
    private UserProduct getUserProduct(UserFestival userFestival, Product product){
        return UserProduct.builder()
                .userFestival(userFestival)
                .product(product)
                .category(ProductCategory.FESTIVAL)
                .build();
    }
    private void checkFestivalProductCount(Festival festival){
        Long count = productJPARepository.findProductCount(festival);
        if (count >= 3){
            throw new BadRequestException(BaseException.PRODUCT_LIMIT_3);
        }
    }
    private Participant findParticipantByUserId(Long userId) {
        return participantJPARepository.findById(userId).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND)
        );
    }
    private UserFestival getUserFestival(Festival festival, Participant participant){
        return UserFestival.builder()
                .festival(festival)
                .participant(participant)
                .status(FestivalStatus.WAITING)
                .build();
    }
    private Festival getFestivalByName(String name){
        return festivalJPARepository.findByName(name).orElseThrow(
                () -> new NotFoundException(BaseException.FESTIVAL_NOT_FOUND)
        );
    }
    private void checkFestivalUserEqualToUser(Festival festival, User user) {
        if (!Objects.equals(festival.getFestivalManager().getId(), user.getId())){
            throw new UnauthorizedException(BaseException.PERMISSION_DENIED_METHOD_ACCESS);
        }
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
