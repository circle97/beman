package com.beman.emotion.repository;

import com.beman.emotion.entity.EmotionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmotionRecordRepository extends JpaRepository<EmotionRecord, Long> {
    
    List<EmotionRecord> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    Optional<EmotionRecord> findByIdAndUserId(Long id, Long userId);
} 