package org.example.spring.user.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Builder
@Getter
public class AuthUserDetails implements UserDetails {

    private Long idx;
    private String username;
    private String password;
    private boolean enable;
    private String role;

    public static AuthUserDetails from(User entity) {
        return AuthUserDetails.builder()
                .idx(entity.getIdx())
                .username(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole())
                .enable(entity.isEnable())
                .build();
    }

    public User toEntity() {
        return User.builder()
                .idx(this.idx)
                .email(this.username)
                .password(this.password)
                .enable(this.enable)
                .role(this.role)
                .build();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
