package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.CourseOrder;
import org.example.fitnessjava.pojo.CourseOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseOrderRepository extends JpaRepository<CourseOrder, Long> {
    List<CourseOrder> findByStatus(CourseOrderStatus status);
    List<CourseOrder> findByUserId(Integer userId);
}
