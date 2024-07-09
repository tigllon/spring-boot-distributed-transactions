package com.distributed.transactions.order.repository;

import com.distributed.transactions.order.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
