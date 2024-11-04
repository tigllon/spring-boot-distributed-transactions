package com.distributed.transactions.order.controller;

import com.distributed.transactions.order.entity.Order;
import com.distributed.transactions.order.dto.CustomerOrder;
import com.distributed.transactions.order.dto.OrderEvent;
import com.distributed.transactions.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @PostMapping("/orders")
    public void createOrder(@RequestBody CustomerOrder customerOrder) {
        Order order = new Order();

        try {
            order.setAmount(customerOrder.getAmount());
            order.setName(customerOrder.getName());
            order.setQuantity(customerOrder.getQuantity());
            order.setStatus("CREATED");
            order = orderRepository.save(order);

            OrderEvent event = new OrderEvent();
            event.setOrder(customerOrder);
            event.setType("ORDER_CREATED");
            customerOrder.setOrderId(order.getId());

            kafkaTemplate.send("new-orders", event);
            log.info("new-orders ORDER_CREATED {}", event);
        } catch (Exception e) {
            order.setStatus("FAILED");
            orderRepository.save(order);
        }
    }
}
