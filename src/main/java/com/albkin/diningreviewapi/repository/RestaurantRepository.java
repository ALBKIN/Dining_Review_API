package com.albkin.diningreviewapi.repository;

import com.albkin.diningreviewapi.model.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
    Restaurant findByName(String name);

    Optional<Restaurant> findRestaurantByNameAndZipCode(String name, String zipCode);
}
