package com.distributed.transactions.stock.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerOrder {

    private Long productId;
    private String name;

    private int quantity;

    private double amount;

    private String paymentMode;

    private Long orderId;

    private String address;
}
