package com.distributed.transactions.payment.service;

import com.distributed.transactions.payment.entity.Payment;
import com.distributed.transactions.payment.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.distributed.transactions.payment.dto.CustomerOrder;
import com.distributed.transactions.payment.dto.OrderEvent;
import com.distributed.transactions.payment.dto.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReversePayment {

    private final PaymentRepository paymentRepository;

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @KafkaListener(topics = "reversed-payments", groupId = "payments-group")
    public void reversePayment(String event) {
        log.info("Inside reverse payment for order {}", event);

        try {
            PaymentEvent paymentEvent = new ObjectMapper().readValue(event, PaymentEvent.class);

            CustomerOrder order = paymentEvent.getOrder();

            Iterable<Payment> payments = this.paymentRepository.findByOrderId(order.getOrderId());

            payments.forEach(p -> {
                p.setStatus("FAILED");
                paymentRepository.save(p);
            });

            OrderEvent orderEvent = new OrderEvent();
            orderEvent.setOrder(paymentEvent.getOrder());
            orderEvent.setType("ORDER_REVERSED");
            kafkaTemplate.send("reversed-orders", orderEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
