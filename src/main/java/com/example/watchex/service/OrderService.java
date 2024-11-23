package com.example.watchex.service;

import com.example.watchex.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface OrderService extends GenericService<Order, Integer> {
    Page<Order> get(int page);

    Order show(Integer id);

}
