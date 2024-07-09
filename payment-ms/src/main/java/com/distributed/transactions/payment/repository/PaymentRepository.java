package com.distributed.transactions.payment.repository;

import com.distributed.transactions.payment.entity.Payment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment, Long> {

    List<Payment> findByOrderId(long orderId);
}
