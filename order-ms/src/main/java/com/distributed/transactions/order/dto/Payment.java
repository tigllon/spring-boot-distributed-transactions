package com.distributed.transactions.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payment {

    private String mode;

    private Long orderId;

    private double amount;
}
