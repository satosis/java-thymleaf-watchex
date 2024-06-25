package com.example.watchex.config;

import com.example.watchex.entity.Admin;
import com.example.watchex.entity.User;
import com.example.watchex.repository.TokenRepository;
import com.example.watchex.service.AdminJwtService;
import com.example.watchex.service.AdminService;
import com.example.watchex.service.JwtService;
import com.example.watchex.service.UserService;
import com.example.watchex.utils.CommonUtils;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AdminJwtService adminJwtService;
    private final UserService userService;
    private final AdminService adminService;
     private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Map<String, String[]> parameters = request.getParameterMap();
        JsonObject jsonObject=new JsonObject();
        for(String key:parameters.keySet()){
            jsonObject.addProperty(key, parameters.get(key)[0]);
        }
        String type = jsonObject.has("type") ? jsonObject.get("type").getAsString() : null;
        if (request.getServletPath().contains("/api/admin/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = CommonUtils.getCookie(request, "Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer+")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
//            boolean isAdmin = adminService.existsByEmail(userEmail);
//            if (isAdmin) {
//                Admin admin = adminService.findByEmail(userEmail);
//                if (adminJwtService.isTokenValid(jwt, admin) && isTokenValid) {
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            admin,
//                            null,
//                            admin.getAuthorities()
//                    );
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                }
//            } else {
                User user = userService.findByEmail(userEmail);
                if (jwtService.isTokenValid(jwt, user) && isTokenValid) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
//            }
        }
        filterChain.doFilter(request, response);
    }
}
