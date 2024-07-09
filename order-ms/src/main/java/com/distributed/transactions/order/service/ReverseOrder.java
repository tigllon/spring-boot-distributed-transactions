package com.distributed.transactions.order.service;

import com.distributed.transactions.order.entity.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.distributed.transactions.order.dto.OrderEvent;
import com.distributed.transactions.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-distributed-transaction
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 5/23/24
 * Time: 05:44
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReverseOrder {

    private final OrderRepository orderRepository;

    @KafkaListener(topics = "reversed-orders", groupId = "orders-group")
    public void reverseOrder(String event) {
        log.info("Inside reverse order for order {}", event);

        try {
            OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);

            Optional<Order> order = orderRepository.findById(orderEvent.getOrder().getOrderId());

            order.ifPresent(o -> {
                o.setStatus("FAILED");
                this.orderRepository.save(o);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
