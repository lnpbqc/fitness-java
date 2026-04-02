package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
    List<Coach> findByFeatured(Boolean isFeatured);
    Optional<Coach> findByOpenid(String openid);
    List<Coach> findByStatus(Coach.Status status);
}
