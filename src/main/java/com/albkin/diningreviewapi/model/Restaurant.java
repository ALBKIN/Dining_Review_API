package com.albkin.diningreviewapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RESTAURANTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="NAME")
    private String name;

    /*
    Constructors, Getters and Setters from Lombok, instead of:

    public String getName() {
        return this.name;
    }

    public void getName(String name) {
        this.name = name;
    }
     */

    private String address1;
    private String city;
    private String state;
    private String zipCode;

    private String phoneNumber;
    private String website;

    @Column(name="PEANUT_SCORE")
    private Integer peanutScore;

    @Column(name="EGG_SCORE")
    private Integer eggScore;

    @Column(name="DAIRY_SCORE")
    private Integer dairyScore;

    @Column(name="OVERALL_SCORE")
    private Integer overallScore;
}
