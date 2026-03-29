package com.buyPhone.util;


import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Clean the token string
        final String token = authHeader.substring(7).trim();

        try {
            Claims claims = jwtUtil.validateToken(token);
            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Ensure the role is present to avoid NullPointerException
                if (role == null) throw new RuntimeException("Role claim is missing");

                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                // Use userId (String) as principal
                var authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // Log the error so you know IF it was an expiry, a signature issue, or a null pointer
            logger.warn("Security Filter: Authentication failed for token. Reason: {}" +  ex.getMessage());
            SecurityContextHolder.clearContext();
            // Option: continue the chain as anonymous, or block. Blocking is safer for protected APIs.
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or Expired Token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
