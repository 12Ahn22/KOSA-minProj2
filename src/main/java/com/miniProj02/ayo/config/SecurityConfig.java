package com.miniProj02.ayo.config;

import com.miniProj02.ayo.handler.AuthFailureHandler;
import com.miniProj02.ayo.handler.AuthSucessHandler;
import com.miniProj02.ayo.member.MemberService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberService memberService;
    private final AuthSucessHandler authSucessHandler;
    private final AuthFailureHandler authFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll() // 이걸 해야 JSP 페이지를 포워딩 가능
                        .requestMatchers("/", "/images/**", "js/**", "/WEB-INF/**", "/intro", "/member/insertForm", "/member/loginForm**", "/member/insert", "/member/login", "/member/duplicate").permitAll()
                        .requestMatchers("/admin/*").hasRole("ADMIN")
                        .anyRequest().authenticated()
                ).exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler((request, response, authException) -> response.sendRedirect("/"))
                        .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/"))) // 로그인&권한 없는 경우 리다이렉팅
                .userDetailsService(memberService)
                .formLogin(form -> form
                        .loginPage("/member/login")
                        .permitAll()
                        .usernameParameter("id")
                        .passwordParameter("password")
                        .loginProcessingUrl("/member/login")
                        .defaultSuccessUrl("/")
                        .successHandler(authSucessHandler)
                        .failureHandler(authFailureHandler))
                .logout((logoutConfig) -> logoutConfig
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }
}
