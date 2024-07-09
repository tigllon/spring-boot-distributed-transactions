package com.distributed.transactions.stock.repository;

import com.distributed.transactions.stock.entity.WareHouse;
import org.springframework.data.repository.CrudRepository;

public interface StockRepository extends CrudRepository<WareHouse, Long> {

    Iterable<WareHouse> findByItem(String item);
}
