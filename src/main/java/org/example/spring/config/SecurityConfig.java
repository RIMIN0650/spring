package org.example.spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity // security 기능 켜기
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final LoginFilter loginFilter;
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        
        http.authorizeHttpRequests(
                (auth) -> auth
                        .requestMatchers("/login", "/signup").permitAll()
        );
        
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);

        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
