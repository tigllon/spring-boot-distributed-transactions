package com.distributed.transactions.stock.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

    private long id;
    private String name;

    private int quantity;
}
