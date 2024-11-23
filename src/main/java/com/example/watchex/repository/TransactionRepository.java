package com.example.watchex.repository;

import com.example.watchex.dto.TransactionRevenueDto;
import com.example.watchex.entity.Product;
import com.example.watchex.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("select p from Transaction p where p.tst_status = :status")
    List<Transaction> getByStatus(Integer status);

    @Query(nativeQuery = true, value =
            "select sum(cast(p.tst_total_money as int)) as totalMoney, DATE(p.created_at) as day from transactions p " +
                    "where p.tst_status = :status and MONTH(p.created_at) = :month group by day")
    List<TransactionRevenueDto> getListRevenueOfMonth(Integer status, Integer month);
}
