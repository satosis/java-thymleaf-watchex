package com.example.watchex.service;


import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public interface GenericService<T, Idt extends Serializable> {
    T delete(Idt id);
    T save(T t);
    Page<T> getList(int pageIndex, int pageSize);
    List<T> getAll();
    T getById(Idt id);
}

