package com.apiserver.greengift.festival.user_festival;

import com.apiserver.greengift.festival.Festival;
import com.apiserver.greengift.festival.constant.FestivalStatus;
import com.apiserver.greengift.user.participant.Participant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_festival_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFestival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    private Festival festival;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FestivalStatus status;

    @Builder
    public UserFestival(Long id, Participant participant, Festival festival, FestivalStatus status){
        this.id = id;
        this.participant = participant;
        this.festival = festival;
        this.status = status;
    }

    public void updateStatus(FestivalStatus festivalStatus){
        this.status = festivalStatus;
    }

}
