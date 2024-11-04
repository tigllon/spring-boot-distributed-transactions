package com.distributed.transactions.stock.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

@Entity
@Getter
@Setter
public class Stock {

    @Id
    private long id;

    @Column (nullable = false)
    private int quantity;

    @Column (nullable = false)
    private String name;
}
