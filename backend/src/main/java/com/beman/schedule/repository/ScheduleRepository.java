package com.beman.schedule.repository;

import com.beman.schedule.entity.Schedule;
import com.beman.schedule.entity.Schedule.ScheduleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    List<Schedule> findByUserIdAndStatusOrderByEventDateAsc(Long userId, ScheduleStatus status);
    
    Page<Schedule> findByUserIdAndStatusOrderByEventDateAsc(Long userId, ScheduleStatus status, Pageable pageable);
    
    @Query("SELECT s FROM Schedule s WHERE s.userId = :userId AND s.status = :status AND s.eventDate >= :startDate ORDER BY s.eventDate ASC")
    List<Schedule> findUpcomingSchedules(@Param("userId") Long userId, @Param("status") ScheduleStatus status, @Param("startDate") LocalDate startDate);
    
    @Query("SELECT s FROM Schedule s WHERE s.userId = :userId AND s.status = :status AND s.eventDate BETWEEN :startDate AND :endDate ORDER BY s.eventDate ASC")
    List<Schedule> findSchedulesByDateRange(@Param("userId") Long userId, @Param("status") ScheduleStatus status, 
                                           @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
} 