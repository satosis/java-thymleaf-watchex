package com.example.watchex.service;

import com.example.watchex.dto.SearchDto;
import com.example.watchex.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@Service
public interface UserService extends GenericService<User, Integer> {
    Page<User> get(int page);

    User show(Integer id) throws UserPrincipalNotFoundException;

    boolean existsByEmail(String email);

    User findByEmail(String email) throws UsernameNotFoundException;
}
