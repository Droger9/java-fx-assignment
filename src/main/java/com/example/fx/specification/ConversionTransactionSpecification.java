package com.example.fx.specification;

import com.example.fx.model.entity.ConversionTransaction;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public final class ConversionTransactionSpecification {

    private ConversionTransactionSpecification() {
    }

    public static Specification<ConversionTransaction> hasTransactionId(UUID transactionId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("transactionId"), transactionId);
    }

    public static Specification<ConversionTransaction> hasTransactionDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("transactionDate"), startOfDay, endOfDay);
    }
}
