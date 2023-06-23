package com.albkin.diningreviewapi.controller;

import com.albkin.diningreviewapi.model.DiningReview;
import com.albkin.diningreviewapi.model.Restaurant;
import com.albkin.diningreviewapi.model.ReviewStatus;
import com.albkin.diningreviewapi.repository.DiningReviewRepository;
import com.albkin.diningreviewapi.repository.RestaurantRepository;
import com.albkin.diningreviewapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@RestController
public class AdminController {
    private final DiningReviewRepository diningReviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");


    public AdminController(DiningReviewRepository diningReviewRepository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.diningReviewRepository = diningReviewRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    // admin method to get the list of all dining reviews that are pending approval.
    @GetMapping("/reviews")
    List<DiningReview> getReviewsByStatus(@RequestParam String requestedStatus) {
        ReviewStatus reviewStatus = ReviewStatus.PENDING;
        try {
            reviewStatus = ReviewStatus.valueOf(requestedStatus.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong requested STATUS search");
        }
        return diningReviewRepository.findByReviewStatus(reviewStatus);
    }

    // admin method to approve or reject a given dining review.
    @PutMapping("/reviews/{id}")
    private DiningReview setAdminReviewStatus(@PathVariable("id") Long id, @RequestParam String wantedStatus) {

        Optional<DiningReview> checkForReviewExistance = this.diningReviewRepository.findById(id);
        if (!checkForReviewExistance.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review doesn't exist");
        }

        Optional<Restaurant> optionalRestaurant = this.restaurantRepository.findById(checkForReviewExistance.get().getRestaurantId());
        if (optionalRestaurant.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Associated restaurant doesn't exist");
        }

        ReviewStatus reviewStatus;
        try {
            reviewStatus = ReviewStatus.valueOf(wantedStatus.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong set STATUS name");
        }
        DiningReview confirmedStatusReview = checkForReviewExistance.get();
        confirmedStatusReview.setReviewStatus(reviewStatus);
        this.diningReviewRepository.save(confirmedStatusReview);
        updateRestaurantDiningReviewScores(optionalRestaurant.get());
        return confirmedStatusReview;
    }

    // method backend process that updates a restaurant’s set of scores, I want to fetch the set of all approved dining reviews belonging to this restaurant
    private void updateRestaurantDiningReviewScores(Restaurant restaurant) {
        List<DiningReview> diningReviewList = this.diningReviewRepository.findDiningReviewsByIdAndStatus(restaurant.getId(), ReviewStatus.ACCEPTED);
        if (diningReviewList.size() == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
            int peanutSum = 0;
            int peanutCount = 0;
            int dairySum = 0;
            int dairyCount = 0;
            int eggSum = 0;
            int eggCount = 0;
            for (DiningReview dR : diningReviewList) {
                if (!ObjectUtils.isEmpty(dR.getPeanutScore())) {
                    peanutSum += dR.getPeanutScore();
                    peanutCount++;
                }
                if (!ObjectUtils.isEmpty(dR.getDairyScore())) {
                    dairySum += dR.getDairyScore();
                    dairyCount++;
                }
                if (!ObjectUtils.isEmpty(dR.getEggScore())) {
                    eggSum += dR.getEggScore();
                    eggCount++;
                }
            }

            int totalCount = peanutCount + dairyCount + eggCount;
            int totalSum = peanutSum + dairySum + eggSum;

            Integer overallScore = (Integer) totalSum / totalCount;
            restaurant.setOverallScore(Integer.valueOf(decimalFormat.format(overallScore)));

            if (peanutCount > 0) {
                Integer peanutScore = (Integer) peanutSum / peanutCount;
                restaurant.setPeanutScore(Integer.valueOf(decimalFormat.format(peanutScore)));
            }
            if (dairyCount > 0) {
                Integer dairyScore = (Integer) dairySum / dairyCount;
                restaurant.setDairyScore(Integer.valueOf(decimalFormat.format(dairyScore)));
            }
            if (eggCount > 0) {
                Integer eggScore = (Integer) eggSum / eggCount;
                restaurant.setEggScore(Integer.valueOf(decimalFormat.format(eggScore)));
            }
            restaurantRepository.save(restaurant);
        }
    }
