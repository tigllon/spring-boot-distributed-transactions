package com.distributed.transactions.order.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderEvent {

    private String type;

    private CustomerOrder order;
}
