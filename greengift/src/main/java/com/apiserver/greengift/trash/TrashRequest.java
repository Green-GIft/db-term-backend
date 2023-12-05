package com.apiserver.greengift.trash;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TrashRequest {

    public record CertificateUser(
            @NotEmpty(message = "이메일은 비어있으면 안됩니다.")
            @Size(max = 100, message = "이메일은 100자 이내여야 합니다.")
            @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
            String email
    ){}

    public record AddTrash(
            @NotEmpty(message = "쓰레기 이름은 비어있으면 안됩니다.")
            @Size(max = 100, message = "쓰레기 이름은 100자 이내여야 합니다.")
            String name,

            @NotEmpty(message = "쓰레기 종류는 비어있으면 안됩니다.")
            @Size(max = 100, message = "쓰레기 종류는 100자 이내여야 합니다.")
            String category
    ){}


}
