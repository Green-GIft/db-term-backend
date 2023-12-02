package com.apiserver.greengift.festival;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FestivalJPARepository extends JpaRepository<Festival, Long> {
    Optional<Festival> findByName(String name);

    List<Festival> findByFestivalManagerId(Long id);
}
