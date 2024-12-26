package com.example.startup.repository;

import com.example.startup.entity.Transaction;
import com.example.startup.entity.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.type = :transactionType")
    Double getTransactionSumByUserIdAndType(@Param("userId") Long userId, @Param("transactionType") TransactionType transactionType);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.type = :transactionType")
    List<Transaction> findByUserIdAndTransactionType(@Param("userId") Long userId, @Param("transactionType") TransactionType transactionType);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionsByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.transactionDate BETWEEN :startDate AND :endDate")
    Double getTransactionSumByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);
}
