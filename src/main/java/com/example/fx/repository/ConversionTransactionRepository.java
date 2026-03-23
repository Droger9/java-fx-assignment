package com.example.fx.repository;

import com.example.fx.model.entity.ConversionTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ConversionTransactionRepository
        extends JpaRepository<ConversionTransaction, UUID>, JpaSpecificationExecutor<ConversionTransaction> {
}
