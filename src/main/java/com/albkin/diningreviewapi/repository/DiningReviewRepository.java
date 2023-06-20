package com.albkin.diningreviewapi.repository;

import com.albkin.diningreviewapi.model.DiningReview;
import com.albkin.diningreviewapi.model.ReviewStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DiningReviewRepository extends CrudRepository<DiningReview, Long> {
    List<DiningReview> findByReviewStatus(ReviewStatus reviewStatus);
}
