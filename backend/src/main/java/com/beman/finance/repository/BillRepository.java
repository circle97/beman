package com.beman.finance.repository;

import com.beman.finance.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    
    Page<Bill> findByUserIdAndTypeAndCategoryAndDateBetween(
            Long userId, Bill.BillType type, String category, 
            LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    Page<Bill> findByUserIdAndTypeAndDateBetween(
            Long userId, Bill.BillType type, 
            LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    Page<Bill> findByUserIdAndCategoryAndDateBetween(
            Long userId, String category, 
            LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    Page<Bill> findByUserIdAndDateBetween(
            Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    @Query("SELECT SUM(b.amount) FROM Bill b WHERE b.userId = :userId AND b.type = :type AND b.date BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByUserIdAndTypeAndDateBetween(
            @Param("userId") Long userId, 
            @Param("type") Bill.BillType type,
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT b.category, SUM(b.amount) as amount, COUNT(b) as count FROM Bill b " +
           "WHERE b.userId = :userId AND b.type = :type AND b.date BETWEEN :startDate AND :endDate " +
           "GROUP BY b.category ORDER BY amount DESC")
    List<Object[]> getCategorySummary(
            @Param("userId") Long userId, 
            @Param("type") Bill.BillType type,
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
} 