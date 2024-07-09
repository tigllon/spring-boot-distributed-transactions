package com.distributed.transactions.stock.service;

import com.distributed.transactions.stock.dto.DeliveryEvent;
import com.distributed.transactions.stock.dto.PaymentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.distributed.transactions.stock.entity.WareHouse;
import com.distributed.transactions.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReversesStock {

    private final StockRepository stockRepository;

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @KafkaListener(topics = "reversed-stock", groupId = "stock-group")
    public void reverseStock(String event) {
        log.info("Inside reverse stock for order {}", event);

        try {
            DeliveryEvent deliveryEvent = new ObjectMapper().readValue(event, DeliveryEvent.class);

            Iterable<WareHouse> inv = this.stockRepository.findByItem(deliveryEvent.getOrder().getItem());

            inv.forEach(i -> {
                i.setQuantity(i.getQuantity() + deliveryEvent.getOrder().getQuantity());
                stockRepository.save(i);
            });

            PaymentEvent paymentEvent = new PaymentEvent();
            paymentEvent.setOrder(deliveryEvent.getOrder());
            paymentEvent.setType("PAYMENT_REVERSED");
            kafkaTemplate.send("reversed-payments", paymentEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
