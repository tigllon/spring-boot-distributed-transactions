package com.distributed.transactions.stock.controller;

import com.distributed.transactions.stock.dto.CustomerOrder;
import com.distributed.transactions.stock.dto.DeliveryEvent;
import com.distributed.transactions.stock.dto.PaymentEvent;
import com.distributed.transactions.stock.dto.Stock;
import com.distributed.transactions.stock.entity.WareHouse;
import com.distributed.transactions.stock.repository.StockRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StockController {

    private final StockRepository stockRepository;

    private final KafkaTemplate<String, DeliveryEvent> kafkaTemplate;

    private final KafkaTemplate<String, PaymentEvent> kafkaPaymentTemplate;

    @KafkaListener(topics = "new-payments", groupId = "payments-group")
    public void updateStock(String paymentEvent) throws JsonProcessingException {
        log.info("Inside update inventory for order {}", paymentEvent);

        DeliveryEvent event = new DeliveryEvent();

        PaymentEvent p = new ObjectMapper().readValue(paymentEvent, PaymentEvent.class);
        CustomerOrder order = p.getOrder();

        try {
            Iterable<WareHouse> inventories = stockRepository.findByItem(order.getItem());

            boolean exists = inventories.iterator().hasNext();

            if (!exists) {
                log.info("Stock not exist so reverting the order");
                throw new Exception("Stock not available");
            }

            inventories.forEach(i -> {
                i.setQuantity(i.getQuantity() - order.getQuantity());
                stockRepository.save(i);
            });

            event.setType("STOCK_UPDATED");
            event.setOrder(p.getOrder());
            kafkaTemplate.send("new-stock", event);
        } catch (Exception e) {
            PaymentEvent pe = new PaymentEvent();
            pe.setOrder(order);
            pe.setType("PAYMENT_REVERSED");
            kafkaPaymentTemplate.send("reversed-payments", pe);
        }
    }

    @PostMapping("/addItems")
    public void addItems(@RequestBody Stock stock) {
        Iterable<WareHouse> items = stockRepository.findByItem(stock.getItem());

        if (items.iterator().hasNext()) {
            items.forEach(i -> {
                i.setQuantity(stock.getQuantity() + i.getQuantity());
                stockRepository.save(i);
            });
        } else {
            WareHouse i = new WareHouse();
            i.setItem(stock.getItem());
            i.setQuantity(stock.getQuantity());
            stockRepository.save(i);
        }
    }
}
