package com.apiserver.greengift.festival.user_festival;

import com.apiserver.greengift.festival.Festival;
import com.apiserver.greengift.user.participant.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserFestivalJPARepository extends JpaRepository<UserFestival, Long> {

    @Query("select u from UserFestival u join fetch u.participant p join fetch u.festival f "+
            "where f = :festival " +
            "and u not in :selectedList " +
            "and u.status = 'READY'")
    List<UserFestival> findUserByFestival(@Param("festival") Festival festival, @Param("selectedList") List<UserFestival> selectedList);

    @Query("select u from UserFestival u where u.festival = :festival " +
            "and u.status = 'READY' order by RAND() limit 3")
    List<UserFestival> findUserFestivalByRandom(@Param("festival") Festival festival);

    @Query("select u from UserFestival u where u.festival = :festival and u.participant = :participant")
    List<UserFestival> findParticipantAndFestival(@Param("participant") Participant participant, @Param("festival") Festival festival);

    @Modifying
    @Query("update UserFestival u set u.status = 'FAIL' where u.id in :nonSelectedIdList")
    void updateFailStatus(@Param("nonSelectedIdList") List<Long> nonSelectedIdList);

    @Query("select u from UserFestival u join fetch u.festival join fetch u.participant p where p.id = :userId")
    List<UserFestival> findByParticipantId(Long userId);

    @Query("select u from UserFestival u join fetch u.participant p join fetch u.festival " +
            "where p.id = :userId and u.festival.id = :festivalId")
    Optional<UserFestival> getUserFestivalByUserIdAndProductId(@Param("userId") Long userId, @Param("festivalId") Long festivalId);
}
