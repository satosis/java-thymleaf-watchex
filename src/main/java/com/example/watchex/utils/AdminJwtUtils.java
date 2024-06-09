package com.example.watchex.utils;

import com.example.watchex.entity.Admin;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
public class AdminJwtUtils {
    private static Logger logger = LoggerFactory.getLogger(AdminJwtUtils.class);
    private static final String ADMIN = "admin";

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired-time}")
    private String expireTime;

    public String generateToken(Admin admin) {
        String token = null;
        try {
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
            // data payload
            builder.claim(ADMIN, admin);
            // token expiration time
            builder.expirationTime(generateExpirationDate());
            JWTClaimsSet claimsSet = builder.build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            // create signer
            JWSSigner signer = new MACSigner(secret.getBytes());
            signedJWT.sign(signer);
            token = signedJWT.serialize();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return token;
    }

    public Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + Long.valueOf(expireTime));
    }

    private JWTClaimsSet getClaimsFromToken(String token) {
        JWTClaimsSet claims = null;
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret.getBytes());
            if (signedJWT.verify(verifier)) {
                claims = signedJWT.getJWTClaimsSet();
            }
        } catch (ParseException | JOSEException e) {
            logger.error(e.getMessage());
        }
        return claims;
    }

    public Admin getAdminFromToken(String token) {
        Admin admin = null;
        try {
            JWTClaimsSet claims = getClaimsFromToken(token);
            if (claims != null && isTokenExpired(claims)) {
                JSONObject jsonObject = (JSONObject) claims.getClaim(ADMIN);
                admin = new ObjectMapper().readValue(jsonObject.toJSONString(), Admin.class);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return admin;
    }

    private Date getExpirationDateFromToken(JWTClaimsSet claims) {
        return claims != null ? claims.getExpirationTime() : new Date();
    }

    private boolean isTokenExpired(JWTClaimsSet claims) {
        return getExpirationDateFromToken(claims).after(new Date());
    }
}
