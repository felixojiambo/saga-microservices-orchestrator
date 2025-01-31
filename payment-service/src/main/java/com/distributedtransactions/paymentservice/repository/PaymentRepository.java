package com.distributedtransactions.paymentservice.repository;

import com.distributedtransactions.paymentservice.domain.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
}
