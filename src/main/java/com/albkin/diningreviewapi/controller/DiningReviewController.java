package com.albkin.diningreviewapi.controller;

import com.albkin.diningreviewapi.model.DiningReview;
import com.albkin.diningreviewapi.model.Restaurant;
import com.albkin.diningreviewapi.model.ReviewStatus;
import com.albkin.diningreviewapi.repository.DiningReviewRepository;
import com.albkin.diningreviewapi.repository.RestauranRepository;
import com.albkin.diningreviewapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class DiningReviewController {

    private final DiningReviewRepository diningReviewRepository;
    private final UserRepository userRepository;
    private final RestauranRepository restauranRepository;

    public DiningReviewController(DiningReviewRepository diningReviewRepository, UserRepository userRepository, RestauranRepository restauranRepository) {
        this.diningReviewRepository = diningReviewRepository;
        this.userRepository = userRepository;
        this.restauranRepository = restauranRepository;
    }

    // method to post review as registered user
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DiningReview createNewReview(@RequestBody DiningReview diningReview) {
        confirmUserReview(diningReview);

        Optional<Restaurant> optionalRestaurant = this.restauranRepository.findById(diningReview.getRestaurantId());
        if (optionalRestaurant.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Can't add a review for non existing restaurant");
        }
        diningReview.setReviewStatus(ReviewStatus.PENDING);
        this.diningReviewRepository.save(diningReview);
        return diningReview;
    }

    // confirm if the review is valid and if user and restaurant exists (only existing users can post dining reviews)
    private void confirmUserReview(DiningReview diningReview) {
    }
}


