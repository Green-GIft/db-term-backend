package com.apiserver.greengift.trash;

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
public class TrashReadService {

    private final TrashJPARepository trashJPARepository;

    public List<TrashResponse.FindTrashByUser> findTrashByUser(User user) {
        List<Trash> trashList = trashJPARepository.findByTrashManagerId(user.getId());
        return trashList.stream().map(it ->
                new TrashResponse.FindTrashByUser(
                        it.getId(),
                        it.getCreatedAt().toString(),
                        it.getName(),
                        it.getCategory().getCategoryName(),
                        it.getUserFestival().getFestival().getName()
                )
        ).toList();
    }

    public List<TrashResponse.FindBestTrashManager> findBestTrashManager(User user) {
        List<Object []> response = trashJPARepository.findBestTrashManager();
        return response.stream().map(it ->
               new TrashResponse.FindBestTrashManager(
                       (String) it[1],
                       (Long) it[0]
               )
        ).toList();
    }
}
