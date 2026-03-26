package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {
}
