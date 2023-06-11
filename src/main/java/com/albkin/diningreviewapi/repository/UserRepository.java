package com.albkin.diningreviewapi.repository;

import com.albkin.diningreviewapi.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
