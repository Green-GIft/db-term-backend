package com.apiserver.greengift.trash;

import com.apiserver.greengift._core.errors.BaseException;
import com.apiserver.greengift._core.errors.exception.BadRequestException;
import com.apiserver.greengift._core.errors.exception.NotFoundException;
import com.apiserver.greengift.festival.constant.FestivalStatus;
import com.apiserver.greengift.festival.user_festival.UserFestival;
import com.apiserver.greengift.festival.user_festival.UserFestivalJPARepository;
import com.apiserver.greengift.trash.constant.TrashCategory;
import com.apiserver.greengift.user.User;
import com.apiserver.greengift.user.constant.Grade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TrashWriteService {

    private final TrashJPARepository trashJPARepository;
    private final UserFestivalJPARepository userFestivalJPARepository;

    public void certificate(User user, TrashRequest.CertificateUser requestDTO, Long festivalId) {
        UserFestival userFestival = findByEmailAndFestivalId(requestDTO.email(), festivalId);
        if (userFestival.getStatus() == FestivalStatus.WAITING) {
            updateGrade(userFestival.getUser());
            userFestival.updateStatus(FestivalStatus.READY);
        }
        else throw new BadRequestException(BaseException.FESTIVAL_CERTIFICATE_INVALID);
    }

    public void addTrash(User user, TrashRequest.AddTrash requestDTO, Long festivalId) {
        UserFestival userFestival = findUserFestivalByUserIdAndProductId(user.getId(), festivalId);
        Trash trash = getTrashEntity(requestDTO, userFestival);
        trashJPARepository.save(trash);
    }

    public void deleteTrash(User user, Long trashId) {
        Trash trash = trashJPARepository.findById(trashId).orElseThrow(
                () -> new NotFoundException(BaseException.TRASH_NOT_FOUND)
        );
        trashJPARepository.delete(trash);
    }

    private void updateGrade(User user) {
        if (user.getTrashCount() == null)
            return;
        user.addTrashCount();
        if (user.getTrashCount()%10 == 0){
            user.updateGrade(Grade.getNextGrade(user.getGrade()));
        }
    }
    private Trash getTrashEntity(TrashRequest.AddTrash requestDTO, UserFestival userFestival) {
        TrashCategory category = TrashCategory.valueOfCategory(requestDTO.category());
        return Trash.builder()
                .userFestival(userFestival)
                .name(requestDTO.name())
                .category(category)
                .build();
    }

    private UserFestival findUserFestivalByUserIdAndProductId(Long userId, Long festivalId) {
        return userFestivalJPARepository.findUserFestivalByUserIdAndProductId(userId, festivalId).orElseThrow(
                () -> new NotFoundException(BaseException.USER_FESTIVAL_NOT_FOUND)
        );
    }

    private UserFestival findByEmailAndFestivalId(String email, Long festivalId) {
        return userFestivalJPARepository.findByEmailAndFestivalId(email, festivalId).orElseThrow(
                () -> new BadRequestException(BaseException.USER_FESTIVAL_NOT_FOUND)
        );
    }
}
