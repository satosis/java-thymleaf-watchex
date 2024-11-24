package com.example.watchex.service.impl;

import com.example.watchex.entity.Rating;
import com.example.watchex.repository.RatingRepository;
import com.example.watchex.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl extends GenericServiceImpl<Rating, Integer> implements RatingService {

    @Autowired
    private RatingRepository repository;

    public Page<Rating> get(int page) {
        return repository.findAll(PageRequest.of(page - 1, 10, Sort.by("id").descending()));
    }

    @Override
    public Rating show(Integer id) {
        Optional<Rating> result = repository.findById(id);
        return result.orElse(null);
    }

}
