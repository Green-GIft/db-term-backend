package com.apiserver.greengift.trash;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrashJPARepository extends JpaRepository<Trash, Long> {
    @Query("select t from Trash t join fetch t.userFestival uf join fetch uf.festival")
    List<Trash> findByTrashManagerId(Long id);

    @Query(value = "select count(*), ut.username from trash_tb t " +
            "join user_festival_tb uf on t.user_festival_id = uf.id " +
            "join user_tb ut on uf.user_id = ut.id " +
            "group by ut.id order by count(*) desc limit 3", nativeQuery = true)
    List<Object []> findBestTrashManager();
}
