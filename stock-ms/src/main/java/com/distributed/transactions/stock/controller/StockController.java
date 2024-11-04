package com.distributed.transactions.stock.controller;

import com.distributed.transactions.stock.dto.Product;
import com.distributed.transactions.stock.entity.Stock;
import com.distributed.transactions.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StockController {
    private final StockRepository stockRepository;

    @PostMapping("/addProduct")
    public void addProduct(@RequestBody Product product) {
        Optional<Stock> stock = stockRepository.findById(product.getId());

        if (stock.isEmpty()) {
            Stock stock1 = new Stock();
            stock1.setName(product.getName());
            stock1.setQuantity(product.getQuantity());
            stock1.setId(product.getId());
            stockRepository.save(stock1);
        } else {
             Stock stock1 = stock.get();
             stock1.setQuantity( stock1.getQuantity() + product.getQuantity());
             stockRepository.save(stock1);
        }
    }

    @GetMapping("/getStocks")
    public List<Stock> getStocks() {
        Iterable<Stock> stockIterable = stockRepository.findAll();
        List<Stock> stockList = new ArrayList<>();
        stockIterable.forEach(stockList::add);
        return stockList;
    }


}
