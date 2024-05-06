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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
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
                /*
		        csrf 토큰 활성화시 사용
		        쿠키를 생성할 때 HttpOnly 태그를 사용하면 클라이언트 스크립트가 보호된 쿠키에 액세스하는 위험을 줄일 수 있으므로 쿠키의 보안을 강화할 수 있다.
		        */
                .csrf((csrf) -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests((authorize) -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll() // 이걸 해야 JSP 페이지를 포워딩 가능
                        .requestMatchers("/","/test" , "/images/**", "js/**", "/WEB-INF/**", "/intro", "/member/insertForm", "/member/loginForm", "/member/insert", "/member/login", "/member/duplicate").permitAll()
                        .requestMatchers("/admin/*").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                                .accessDeniedHandler((request, response, authException) -> response.sendRedirect("/"))
//                        .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/"))
                ) // 로그인&권한 없는 경우 리다이렉팅
                .formLogin(form -> form
                        .loginPage("/member/loginForm")
                        .permitAll()
                        .usernameParameter("id")
                        .passwordParameter("password")
                        .loginProcessingUrl("/member/login")
                        .defaultSuccessUrl("/")
                        .successHandler(authSucessHandler)
                        .failureHandler(authFailureHandler)
                )
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
