package com.uptc.edu.main.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements Filter {

    private final Map<String, Long> requestCounts = new ConcurrentHashMap<>();
    private static final long LIMIT = 100; // máximo de peticiones por hora
    private static final long WINDOW = 60 * 60 * 1000; // 1 hora en ms
    private final Map<String, Long> timestamps = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String ip = req.getRemoteAddr();

        long now = System.currentTimeMillis();
        timestamps.putIfAbsent(ip, now);
        if (now - timestamps.get(ip) > WINDOW) {
            requestCounts.put(ip, 0L);
            timestamps.put(ip, now);
        }

        requestCounts.putIfAbsent(ip, 0L);
        if (requestCounts.get(ip) >= LIMIT) {
            ((HttpServletResponse) response).setStatus(429);
            response.getWriter().write("Demasiadas peticiones, intenta más tarde.");
            return;
        }

        requestCounts.put(ip, requestCounts.get(ip) + 1);
        chain.doFilter(request, response);
    }
}
