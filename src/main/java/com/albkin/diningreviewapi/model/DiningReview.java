package com.albkin.diningreviewapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class DiningReview {

    @Id
    @GeneratedValue
    private Long id;

    private String reviewByUsername;
    private Long RestaurantId;
    private Integer peanutScore;
    private Integer eggScore;
    private Integer dairyScore;
    private String commentary;
    private ReviewStatus reviewStatus;

}
