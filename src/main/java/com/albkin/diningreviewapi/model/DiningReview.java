package com.albkin.diningreviewapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="DINING_REVIEWS")
@Data
public class DiningReview {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="REVIEW_BY")
    private String reviewByUsername;

    @Column(name="RESTAURANT_ID")
    private Long RestaurantId;

    @Column(name="PEANUT_SCORE")
    private Integer peanutScore;

    @Column(name="EGG_SCORE")
    private Integer eggScore;

    @Column(name="DAIRY_SCORE")
    private Integer dairyScore;

    @Column(name="COMMENTARY")
    private String commentary;

    @Column(name="REVIEW_STATUS")
    private ReviewStatus reviewStatus;


}
