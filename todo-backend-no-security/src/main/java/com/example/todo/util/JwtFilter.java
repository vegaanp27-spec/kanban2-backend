package com.example.todo.util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;

@Component
@Order(1)
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        String auth = req.getHeader("Authorization");
        if (auth == null || auth.isBlank()) {
            // No Authorization header: allow as public request (controllers can check for authenticated user via request attribute)
            chain.doFilter(request, response);
            return;
        }

        if (!auth.startsWith("Bearer ")) {
            unauthorized(res, "Missing or invalid Authorization header");
            return;
        }

        String token = auth.substring(7);
        if (!jwtUtil.validateToken(token)) {
            unauthorized(res, "Invalid or expired token");
            return;
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            unauthorized(res, "Invalid token subject");
            return;
        }

        // set user id as request attribute for downstream handlers if needed
        req.setAttribute("userId", userId);

        // Also set Authentication in SecurityContext so controllers/services can use Spring Security if needed
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private void unauthorized(HttpServletResponse res, String message) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json");
        String body = "{\"error\":\"" + message.replace("\"","\\\"") + "\"}";
        res.getWriter().write(body);
    }

    private boolean isPublicPath(String path) {
        // Public API endpoints
        if (path == null) return true;
        // allow login/register
        if (path.startsWith("/api/users/login") || path.startsWith("/api/users/register")) return true;
        // allow Swagger and OpenAPI endpoints
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.equals("/swagger-ui.html") || path.startsWith("/swagger-resources")) return true;
        // allow root
        if (path.equals("/") || path.startsWith("/actuator")) return true;
        return false;
    }
}
