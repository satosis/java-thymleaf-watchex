package com.example.watchex.service.impl;

import com.example.watchex.entity.Order;
import com.example.watchex.repository.OrderRepository;
import com.example.watchex.repository.TransactionRepository;
import com.example.watchex.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends GenericServiceImpl<Order, Integer> implements OrderService {

    @Autowired
    private OrderRepository repository;

    public Page<Order> get(int page) {
        return repository.findAll(PageRequest.of(page, 10, Sort.by("id").descending()));
    }

    @Override
    public Order show(Integer id) {
        Optional<Order> result = repository.findById(id);
        return result.orElse(null);
    }

}
