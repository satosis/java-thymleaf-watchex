package com.example.watchex.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    //    private final LogoutHandler logoutHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/user/auth/**", "/bower_components/**", "/component/**", "/static/**", "/resources/**", "/favicon.ico", "/plugins/**",
                 "/ckeditor/**",
                 "/dist/**",
                 "/js/**",
                 "/plugins/**",
                 "/tagsinput/**",
                 "/toastr/**",
                 "/uploads/**",
                 "/vendor/**",
                 "/video/**",
                 "/view/**",
                 "/product/**",
                 "/admin-auth/**"
                )
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();

        return http.build();
    }
}
