package com.example.watchex.service;

import com.example.watchex.dto.TransactionRevenueDto;
import com.example.watchex.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService extends GenericService<Transaction, Integer> {
    Page<Transaction> get(int page);

    Transaction show(Integer id);

    List<Transaction> getByStatus(Integer status);

    List<TransactionRevenueDto> getListRevenueOfMonthByStatus(Integer status);

}
