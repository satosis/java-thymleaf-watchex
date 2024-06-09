package com.example.watchex.service;

import com.example.watchex.entity.Admin;
import com.example.watchex.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Service
public interface AdminService extends GenericService<Admin, Integer> {
    Page<Admin> get(int page);

    Admin show(Integer id) throws UserPrincipalNotFoundException;

    boolean existsByEmail(String email);

    Admin findByEmail(String email) throws UsernameNotFoundException;
}
