package com.apiserver.greengift.user.festival_manager;

import com.apiserver.greengift.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(value = "festival_manager")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalManager extends User {

    @Builder
    public FestivalManager(Long id, String username, String email, String password){
        super(id, username, email, password);
    }
}
