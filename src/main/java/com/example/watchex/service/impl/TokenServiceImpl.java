package com.example.watchex.service.impl;

import com.example.watchex.entity.Token;
import com.example.watchex.repository.TokenRepository;
import com.example.watchex.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    public Token createToken(Token token) {
        return tokenRepository.saveAndFlush(token);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void revokeToken(Token token) {
        tokenRepository.delete(token);
    }

    @Override
    public Token save(Token token) { return tokenRepository.save(token);};

    @Override
    public List<Token> findAllValidTokenByUser(Integer id) { return tokenRepository.findAllValidTokenByUser(id);};

    @Override
    public List saveAll(List tokens) { return tokenRepository.saveAll(tokens);};
}
