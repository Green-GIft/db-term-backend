package com.apiserver.greengift.user;

import com.apiserver.greengift.user.constant.Grade;
import com.apiserver.greengift.user.festival_manager.FestivalManager;
import com.apiserver.greengift.user.participant.Participant;
import com.apiserver.greengift.user.trash_manager.TrashManager;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequest {

    public record SignUpDTO (

            @NotEmpty(message = "역할은 비어있으면 안됩니다.")
            @Size(max = 100, message = "역할은 100자 이내여야 합니다.")
            String role,

            @NotEmpty(message = "이름은 비어있으면 안됩니다.")
            @Size(min = 2, max = 8, message = "이름은 2에서 8자 이내여야 합니다.")
            String username,

            @NotEmpty(message = "이메일은 비어있으면 안됩니다.")
            @Size(max = 100, message = "이메일은 100자 이내여야 합니다.")
            @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
            String email,

            @NotEmpty(message = "패스워드는 비어있으면 안됩니다.")
            @Size(min = 8, max = 20, message = "패스워드는 8에서 20자 이내여야 합니다.")
            @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
            String password,

            @NotEmpty(message = "패스워드2는 비어있으면 안됩니다.")
            @Size(min = 8, max = 20, message = "패스워드는 8에서 20자 이내여야 합니다.")
            @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
            String password2
    ){
        public Participant toParticipant(String encodedPassword) {
            return Participant.builder()
                    .email(email)
                    .password(encodedPassword)
                    .username(username)
                    .mileage(0L)
                    .grade(Grade.SEED)
                    .build();
        }
        public FestivalManager toFestivalManager(String encodedPassword) {
            return FestivalManager.builder()
                    .email(email)
                    .password(encodedPassword)
                    .username(username)
                    .build();
        }
        public TrashManager toTrashManager(String encodedPassword) {
            return TrashManager.builder()
                    .email(email)
                    .password(encodedPassword)
                    .username(username)
                    .build();
        }
    }

    public record LoginDTO(

            @NotEmpty(message = "이메일은 비어있으면 안됩니다.")
            @Size(max = 100, message = "이메일은 100자 이내여야 합니다.")
            @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
            String email,

            @NotEmpty(message = "패스워드는 비어있으면 안됩니다.")
            @Size(min = 8, max = 20, message = "8에서 20자 이내여야 합니다.")
            @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
            String password
    ){
    }

}
