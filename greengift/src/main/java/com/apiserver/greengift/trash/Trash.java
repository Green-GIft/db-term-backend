package com.apiserver.greengift.trash;

import com.apiserver.greengift.festival.user_festival.UserFestival;
import com.apiserver.greengift.trash.constant.TrashCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "trash_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserFestival userFestival;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TrashCategory category;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Builder
    public Trash(Long id, UserFestival userFestival, String name, TrashCategory category) {
        this.id = id;
        this.userFestival = userFestival;
        this.name = name;
        this.category = category;
        this.createdAt = LocalDate.now();
    }
}
