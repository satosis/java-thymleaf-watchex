package com.example.watchex.service.impl;


import com.example.watchex.exceptions.ValidException;
import com.example.watchex.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Transactional
public class GenericServiceImpl<T, Idt extends Serializable> implements GenericService<T, Idt> {
    @Autowired
    public JpaRepository<T, Idt> repository;
    @Autowired
    public EntityManager manager;

    public T delete(Idt id) {
        T result = this.repository.findById(id).orElse(null);
        if (result != null) {
            this.repository.deleteById(id);
        }

        return result;
    }

    public T save(T t) {
        return this.repository.save(t);
    }

    public Page<T> getList(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return this.repository.findAll(pageable);
    }

    @Override
    public List<T> getAll() {
        return this.repository.findAll();
    }

    public T getById(Idt id) {
        return this.repository.findById(id).orElseThrow(()-> new ValidException("Không tìm thấy đối tượng !"));
    }
}