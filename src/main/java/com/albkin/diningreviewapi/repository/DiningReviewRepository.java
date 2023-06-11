package com.albkin.diningreviewapi.repository;

import org.springframework.data.repository.CrudRepository;
import com.albkin.diningreviewapi.model.DiningReview;

public interface DiningReviewRepository extends CrudRepository<DiningReview, Long> {
}
