package com.apiserver.greengift.user;

import com.apiserver.greengift._core.errors.BaseException;
import com.apiserver.greengift._core.errors.exception.BadRequestException;
import com.apiserver.greengift._core.errors.exception.NotFoundException;
import com.apiserver.greengift._core.security.JWTProvider;
import com.apiserver.greengift.user.constant.Role;
import com.apiserver.greengift.user.festival_manager.FestivalManagerJPARepository;
import com.apiserver.greengift.user.participant.Participant;
import com.apiserver.greengift.user.participant.ParticipantJPARepository;
import com.apiserver.greengift.user.trash_manager.TrashManagerJPARepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserJPARepository userJPARepository;
    private final ParticipantJPARepository participantJPARepository;
    private final TrashManagerJPARepository trashManagerJPARepository;
    private final FestivalManagerJPARepository festivalManagerJPARepository;

    @Transactional
    public void signup(UserRequest.SignUpDTO requestDTO){
        checkEmailAlreadyExist(requestDTO.email());
        checkPasswordIsSame(requestDTO.password(), requestDTO.password2());
        Role role = Role.valueOfRole(requestDTO.role());
        String encodedPassword = passwordEncoder.encode(requestDTO.password());
        saveUser(requestDTO, role, encodedPassword);
    }

    public String login(UserRequest.LoginDTO requestDTO){
        User user = findUserByRequest(requestDTO);
        verifyPassword(requestDTO, user);
        return JWTProvider.create(user);
    }

    public UserResponse.FindGrade getUserGrade(User user){
        Participant participant = participantJPARepository.findById(user.getId()).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND)
        );

        return new UserResponse.FindGrade(
                participant.getGrade().getGradeName(),
                participant.getMileage()
        );
    }

    private void verifyPassword(UserRequest.LoginDTO requestDTO, User user) {
        if (!passwordEncoder.matches(requestDTO.password(), user.getPassword())) {
            throw new BadRequestException(BaseException.USER_PASSWORD_WRONG);
        }
    }
    private User findUserByRequest(UserRequest.LoginDTO requestDTO) {
        return userJPARepository.findByEmail(requestDTO.email()).orElseThrow(
                () -> new BadRequestException(BaseException.USER_EMAIL_NOT_FOUND)
        );
    }
    private void checkEmailAlreadyExist(String email) {
        Optional<User> userOptional = userJPARepository.findByEmail(email);
        if (userOptional.isPresent()){
            throw new BadRequestException(BaseException.USER_DUPLICATED_EMAIL);
        }
    }
    private void checkPasswordIsSame(String password, String password2) {
        boolean isEqual = Objects.equals(password, password2);
        if (!isEqual){
            throw new BadRequestException(BaseException.USER_PASSWORD_NOT_SAME);
        }
    }
    private void saveUser(UserRequest.SignUpDTO requestDTO, Role role, String encodedPassword) {
        if (role == Role.PARTICIPANT) {
            participantJPARepository.save(requestDTO.toParticipant(encodedPassword));
        }
        else if (role == Role.TRASH_MANAGER){
            trashManagerJPARepository.save(requestDTO.toTrashManager(encodedPassword));
        }
        else if (role == Role.FESTIVAL_MANAGER){
            festivalManagerJPARepository.save(requestDTO.toFestivalManager(encodedPassword));
        }
    }
}
