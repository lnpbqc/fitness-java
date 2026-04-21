package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.ImageUploadRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageUploadRecordRepository extends JpaRepository<ImageUploadRecord, Long> {
}
