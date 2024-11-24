package com.example.watchex.service.impl;

import com.example.watchex.dto.TransactionRevenueDto;
import com.example.watchex.entity.Transaction;
import com.example.watchex.repository.TransactionRepository;
import com.example.watchex.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl extends GenericServiceImpl<Transaction, Integer> implements TransactionService {

    @Autowired
    private TransactionRepository repository;

    public Page<Transaction> get(int page) {
        return repository.findAll(PageRequest.of(page - 1, 10, Sort.by("id").descending()));
    }

    @Override
    public Transaction show(Integer id) {
        Optional<Transaction> result = repository.findById(id);
        return result.orElse(null);
    }

    @Override
    public List<Transaction> getByStatus(Integer status) {
        return repository.getByStatus(status);
    }

    @Override
    public List<TransactionRevenueDto> getListRevenueOfMonthByStatus(Integer status){

        Integer month = Calendar.getInstance().get(Calendar.MONTH)+1;
        return repository.getListRevenueOfMonth(status, month);
    }
}
