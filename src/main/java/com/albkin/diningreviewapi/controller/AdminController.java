package com.albkin.diningreviewapi.controller;

import com.albkin.diningreviewapi.model.DiningReview;
import com.albkin.diningreviewapi.model.ReviewStatus;
import com.albkin.diningreviewapi.repository.DiningReviewRepository;
import com.albkin.diningreviewapi.repository.RestauranRepository;
import com.albkin.diningreviewapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class AdminController {
    private final DiningReviewRepository diningReviewRepository;
    private final UserRepository userRepository;
    private final RestauranRepository restauranRepository;

    public AdminController(DiningReviewRepository diningReviewRepository, UserRepository userRepository, RestauranRepository restauranRepository) {
        this.diningReviewRepository = diningReviewRepository;
        this.userRepository = userRepository;
        this.restauranRepository = restauranRepository;
    }

// admin method to get the list of all dining reviews that are pending approval.
    @GetMapping("/reviews")
    List<DiningReview> getReviewsByStatus(@RequestParam String requestedStatus) {
        ReviewStatus reviewStatus = ReviewStatus.PENDING;
        try {
            reviewStatus = ReviewStatus.valueOf(requestedStatus.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        }
        return diningReviewRepository.findByReviewStatus(reviewStatus);
    }
}
