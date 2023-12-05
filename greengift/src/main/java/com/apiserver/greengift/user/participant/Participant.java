package com.apiserver.greengift.user.participant;

import com.apiserver.greengift.user.User;
import com.apiserver.greengift.user.constant.Grade;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(value = "participant")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant extends User {

    @Builder
    public Participant(Long id, String username, String email, String password, Grade grade, Long mileage, Long trashCount){
        super(id, username, email, password, grade, mileage, trashCount);
    }
}
