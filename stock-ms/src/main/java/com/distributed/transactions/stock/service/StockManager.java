package com.distributed.transactions.stock.service;

import com.distributed.transactions.stock.dto.CustomerOrder;
import com.distributed.transactions.stock.dto.PaymentEvent;
import com.distributed.transactions.stock.entity.Stock;
import com.distributed.transactions.stock.repository.StockRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockManager {
    private final StockRepository stockRepository;
    private final KafkaTemplate<String, PaymentEvent> kafkaPaymentTemplate;

    @KafkaListener(topics = "new-payments", groupId = "payments-group")
    public void updateStock(String paymentEvent) throws JsonProcessingException {
        log.info("Inside update inventory for order {}", paymentEvent);

        PaymentEvent p = new ObjectMapper().readValue(paymentEvent, PaymentEvent.class);
        CustomerOrder order = p.getOrder();

        try {
            Optional<Stock> stock = stockRepository.findById(order.getProductId());
            if (stock.isEmpty()) {
                log.info("Stock not exist so reverting the order");
                throw new Exception("Stock not available");
            }

            Stock stock1 = stock.get();
            if (stock1.getQuantity() < order.getQuantity()){
                log.info("The ordered quantity is more than available quantity");
                throw new Exception("Ordered quantity is greater than available");
            }
            stock1.setQuantity(stock1.getQuantity()-order.getQuantity());
            stockRepository.save(stock1);
        } catch (Exception e) {
            PaymentEvent pe = new PaymentEvent();
            pe.setOrder(order);
            pe.setType("PAYMENT_REVERSED");
            kafkaPaymentTemplate.send("reversed-payments", pe);
        }
    }
}
