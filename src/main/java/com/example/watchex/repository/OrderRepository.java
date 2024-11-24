package com.example.watchex.repository;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.entity.Order;
import com.example.watchex.entity.Rating;
import com.example.watchex.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT p FROM Order p " +
            "WHERE p.od_transaction_id = :transaction " +
            "order by p.createdAt desc")
    List<Order> getByTransaction(Transaction transaction);
}
