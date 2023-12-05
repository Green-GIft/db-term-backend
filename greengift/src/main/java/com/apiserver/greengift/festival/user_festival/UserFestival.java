package com.apiserver.greengift.festival.user_festival;

import com.apiserver.greengift.festival.Festival;
import com.apiserver.greengift.festival.constant.FestivalStatus;
import com.apiserver.greengift.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_festival_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFestival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Festival festival;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FestivalStatus status;

    @Builder
    public UserFestival(Long id, User user, Festival festival, FestivalStatus status){
        this.id = id;
        this.user = user;
        this.festival = festival;
        this.status = status;
    }

    public void updateStatus(FestivalStatus festivalStatus){
        this.status = festivalStatus;
    }

}
