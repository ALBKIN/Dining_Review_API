package com.albkin.diningreviewapi.repository;

import com.albkin.diningreviewapi.model.Restaurant;
import org.springframework.data.repository.CrudRepository;

public interface RestauranRepository extends CrudRepository<Restaurant, Long> {
}
