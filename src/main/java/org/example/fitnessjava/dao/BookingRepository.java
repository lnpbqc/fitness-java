package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.penddingEntity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByUserId(Integer userId);

    Optional<Booking> findByIdAndUserId(Integer id, Integer userId);
}
