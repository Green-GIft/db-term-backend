package com.apiserver.greengift.user;

import com.apiserver.greengift.user.constant.Grade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_tb")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column
    private Long mileage;

    @Column(name = "trash_count")
    private Long trashCount;

    @Transient
    public String getDtype(){
        DiscriminatorValue val = this.getClass().getAnnotation(DiscriminatorValue.class);
        return val == null ? null : val.value();
    }

    public void addTrashCount(){
        this.trashCount = this.trashCount + 1;
    }

    public void updateGrade(Grade grade){
        this.grade = grade;
    }

    public void reduceMileage(Long price){
        this.mileage = this.mileage - price;
    }
}
