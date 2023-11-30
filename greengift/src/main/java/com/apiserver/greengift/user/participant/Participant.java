package com.apiserver.greengift.user.participant;

import com.apiserver.greengift.user.User;
import com.apiserver.greengift.user.constant.Grade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(value = "participant")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant extends User {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(nullable = false)
    private Long mileage;

    @Builder
    public Participant(Long id, String username, String email, String password, Grade grade, Long mileage){
        super(id, username, email, password);
        this.grade = grade;
        this.mileage = mileage;
    }
}
