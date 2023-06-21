package com.albkin.diningreviewapi.controller;

import com.albkin.diningreviewapi.model.DiningReview;
import com.albkin.diningreviewapi.model.Restaurant;
import com.albkin.diningreviewapi.model.ReviewStatus;
import com.albkin.diningreviewapi.repository.DiningReviewRepository;
import com.albkin.diningreviewapi.repository.RestaurantRepository;
import com.albkin.diningreviewapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class AdminController {
    private final DiningReviewRepository diningReviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

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
        return confirmedStatusReview;
    }
}
