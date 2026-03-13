package com.example.fx.repository;

import com.example.fx.model.entity.ConversionTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ConversionTransactionRepository extends JpaRepository<ConversionTransaction, UUID> {

    Page<ConversionTransaction> findByTransactionId(UUID transactionId, Pageable pageable);

    Page<ConversionTransaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}