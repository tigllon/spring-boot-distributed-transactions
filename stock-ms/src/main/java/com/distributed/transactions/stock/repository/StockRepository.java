package com.distributed.transactions.stock.repository;

import com.distributed.transactions.stock.entity.Stock;
import org.springframework.data.repository.CrudRepository;

public interface StockRepository extends CrudRepository<Stock, Long> {

}
