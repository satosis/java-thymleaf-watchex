package com.example.watchex.service;

import com.example.watchex.entity.Order;
import com.example.watchex.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService extends GenericService<Order, Integer> {
    Page<Order> get(int page);

    Order show(Integer id);

    List<Order> getByTransaction(Transaction transaction);

}
