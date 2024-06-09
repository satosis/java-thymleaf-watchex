package com.example.watchex.service;

import com.example.watchex.entity.Token;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TokenService {

    Token createToken(Token token);

    Optional<Token> findByToken(String token);

    void revokeToken(Token token);

    Token save(Token token);

    List<Token> findAllValidTokenByUser(Integer id);

    List<Token> saveAll(List token);
}
