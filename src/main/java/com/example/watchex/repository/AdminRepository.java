package com.example.watchex.repository;

import com.example.watchex.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    @Query("SELECT u FROM Admin u WHERE u.email = :email")
    Admin findByEmail(@Param("email") String email);

    @Query("SELECT count(u) FROM Admin u WHERE u.email = :email")
    int existsByEmail(String email);
}
