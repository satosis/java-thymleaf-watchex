package com.example.watchex.service.impl;

import com.example.watchex.entity.Admin;
import com.example.watchex.repository.AdminRepository;
import com.example.watchex.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl extends GenericServiceImpl<Admin, Integer> implements AdminService {

    @Autowired
    private AdminRepository repository;

    public Page<Admin> get(int page) {
        return repository.findAll(PageRequest.of(page, 10, Sort.by("id").descending()));
    }

    @Override
    public Admin show(Integer id) throws UserPrincipalNotFoundException {
        Optional<Admin> result = repository.findById(id);
        return result.orElse(null);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email) > 0;
    }

    @Override
    public Admin findByEmail(String email) {
        Optional<Admin> result = Optional.ofNullable(repository.findByEmail(email));
        return result.orElse(null);
    }
}
