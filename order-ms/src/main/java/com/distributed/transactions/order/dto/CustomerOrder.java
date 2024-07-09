package com.distributed.transactions.order.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerOrder {
    private String item;

    private int quantity;

    private double amount;

    private String paymentMode;

    private long orderId;

    private String address;
}
