package com.miniProj02.ayo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO  implements UserDetails {
    private String id;
    private String name;
    private String password;
    private String birthdate;
    private String address;
    private String phone;
    private String gender;
    private Integer auth;
    private String account_locked; // 계정 잠금 여부
    private String account_locked_time; // 계정 잠긴 시간
    private Integer login_count; // 계정 로그인 시도 횟수

    public MemberVO hashPassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
        return this;
    }

    public String getAuthName(){
        return switch (this.auth){
            case 1 -> "ADMIN";
            default -> "USER";
        };
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collections = new ArrayList<>();

        String authString = switch (auth){
            case 1 -> "ADMIN";
            default -> "USER";
        };
        collections.add(new SimpleGrantedAuthority("ROLE_" + authString));
        return collections;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // true = "N" , false ="Y"
        return account_locked.equals("N");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
