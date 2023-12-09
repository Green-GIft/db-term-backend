package com.apiserver.greengift.user.participant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipantJPARepository extends JpaRepository<Participant, Long> {
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Participant p set p.mileage = p.mileage+10  " +
            "where p.id in :nonSelected")
    void updateMileage(@Param("nonSelected") List<Long> nonSelected);
}
