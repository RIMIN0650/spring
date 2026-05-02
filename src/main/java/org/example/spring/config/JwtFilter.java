package org.example.spring.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.spring.user.model.AuthUserDetails;
import org.example.spring.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        return path.startsWith("/login") ||
                path.startsWith("/signup");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("ATOKEN")) {
                    Long userIdx = JwtUtil.getUserIdx(cookie.getValue());

                    String username = JwtUtil.getUsername(cookie.getValue());
                    String role = JwtUtil.getRole(cookie.getValue());
                    System.out.println(username);
                    System.out.println(role);

                    AuthUserDetails authUserDetails = AuthUserDetails.builder()
                            .idx(userIdx)
                            .username(username)
                            .role(role)
                            .build();

                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            authUserDetails,
                            null,
                            List.of(new SimpleGrantedAuthority(role))
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            }
        }
        filterChain.doFilter(request, response);
    }

}
