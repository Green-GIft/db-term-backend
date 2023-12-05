package com.apiserver.greengift.festival.user_festival;

import com.apiserver.greengift.festival.Festival;
import com.apiserver.greengift.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserFestivalJPARepository extends JpaRepository<UserFestival, Long> {

    @Query("select u from UserFestival u join fetch u.user p join fetch u.festival f "+
            "where f = :festival " +
            "and u not in :selectedList " +
            "and u.status = 'READY'")
    List<UserFestival> findUserByFestival(@Param("festival") Festival festival, @Param("selectedList") List<UserFestival> selectedList);

    @Query("select u from UserFestival u where u.festival = :festival " +
            "and u.status = 'READY' order by RAND() limit 3")
    List<UserFestival> findUserFestivalByRandom(@Param("festival") Festival festival);

    @Query("select u from UserFestival u join fetch u.festival join fetch u.user p where p.id = :userId")
    List<UserFestival> findByParticipantId(Long userId);

    @Query("select u from UserFestival u where u.festival = :festival and u.user = :user")
    List<UserFestival> findParticipantAndFestival(@Param("user") User user, @Param("festival") Festival festival);

    @Query("select u from UserFestival u join fetch u.user p join fetch u.festival " +
            "where p.id = :userId and u.festival.id = :festivalId")
    Optional<UserFestival> findUserFestivalByUserIdAndProductId(@Param("userId") Long userId, @Param("festivalId") Long festivalId);

    @Query("select u from UserFestival u " +
            "where u.user.email = :email and u.festival.id = :festivalId")
    Optional<UserFestival> findByEmailAndFestivalId(@Param("email") String email, @Param("festivalId") Long festivalId);

    @Modifying
    @Query("update UserFestival u set u.status = 'FAIL' where u in :nonSelectedIdList")
    void updateFailStatus(List<UserFestival> nonSelectedIdList);
    
    @Modifying
    @Query("update UserFestival u set u.status = 'SUCCESS' where u in :userFestivalList")
    void updateSuccessStatus(List<UserFestival> userFestivalList);
}
