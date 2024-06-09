package com.example.watchex.service;

import com.example.watchex.entity.Keyword;
import com.example.watchex.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KeywordService {
    @Autowired
    private KeywordRepository keywordRepository;

    public Page<Keyword> get(int page) {
        return keywordRepository.findAll(PageRequest.of(page, 10, Sort.by("id").descending()));
    }

    public List<Keyword> getAll() {
        return keywordRepository.findAll();
    }
    public void save(Keyword keyword) {
        keywordRepository.save(keyword);
    }

    public Keyword show(Integer id) throws ClassNotFoundException {
        Optional<Keyword> result = keywordRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new ClassNotFoundException("Keyword not found");
    }

    public void delete(Integer id) {
        keywordRepository.deleteById(id);
    }
}
