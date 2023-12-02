package com.apiserver.greengift.festival;

import com.apiserver.greengift.user.festival_manager.FestivalManager;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "festival_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Festival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private FestivalManager festivalManager;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_finished", nullable = false)
    private Boolean isFinished;

    @Lob
    private String image;

    @Builder
    public Festival(Long id, FestivalManager festivalManager, String name, LocalDate startDate, LocalDate endDate, Boolean isFinished, String image){
        this.id = id;
        this.festivalManager = festivalManager;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isFinished = isFinished;
        this.image = image;
    }

    public void finished(){
        this.isFinished = true;
    }
}
