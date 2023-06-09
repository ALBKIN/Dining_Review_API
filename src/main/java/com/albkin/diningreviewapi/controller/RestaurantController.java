package com.albkin.diningreviewapi.controller;

import com.albkin.diningreviewapi.model.Restaurant;
import com.albkin.diningreviewapi.repository.RestaurantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;
    private final Pattern zipCodePattern = Pattern.compile("\\d{5}");


    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurant addNewRestaurant(@RequestBody Restaurant restaurant) {
        confirmNewRestaurant(restaurant);

        return restaurantRepository.save(restaurant);
    }

    private void confirmNewRestaurant(Restaurant restaurant) {
        // check if the request to add restaurant contains a name which is mandatory minimum
        if (ObjectUtils.isEmpty(restaurant.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        validateZipCode(restaurant.getZipCode());

        Optional<Restaurant> checkRestaurant = this.restaurantRepository.findRestaurantByNameAndZipCode(restaurant.getName(), restaurant.getZipCode());
        if (checkRestaurant.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    // check if the zip code is in proper 5 digit format and no special characters
    private void validateZipCode(String zipCode) {
        if (!zipCodePattern.matcher(zipCode).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    // method to fetch the details of a restaurant, given its unique Id.
    @GetMapping("{id}")
    public Optional<Restaurant> getRestaurantById(@PathVariable("id") Long id) {
        Optional<Restaurant> optionalRestaurant = this.restaurantRepository.findById(id);
        if (!optionalRestaurant.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return optionalRestaurant;
    }

    // method to to fetch restaurants that match a given zip code and that also have at least one user-submitted score for a given allergy sorted in descending order.
    @GetMapping("/search")
    public Iterable<Restaurant> getRestaurantsByZipCodeAndAllergyScore(@RequestParam String zipCode, @RequestParam String allergy) {
        validateZipCode(zipCode);

        Iterable<Restaurant> restaurants = Collections.EMPTY_LIST;
        if (allergy.equalsIgnoreCase("peanut")) {
            restaurants = this.restaurantRepository.findRestaurantByZipCodeAndPeanutScoreNotNullOrderByPeanutScore(zipCode);
        } else if (allergy.equalsIgnoreCase("dairy")) {
            restaurants = this.restaurantRepository.findRestaurantByZipCodeAndDairyScoreNotNullOrderByPeanutScore(zipCode);
        } else if (allergy.equalsIgnoreCase("egg")) {
            restaurants = this.restaurantRepository.findRestaurantByZipCodeAndEggScoreNotNullOrderByPeanutScore(zipCode);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return restaurants;
    }
}
