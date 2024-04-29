package com.miniProj02.ayo.handler;

import com.miniProj02.ayo.member.MemberMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthSucessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final MemberMapper memberMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        // authentication에 로그인한 사용자 정보
                                        Authentication authentication) throws IOException, ServletException {
        // 로그인 성공 시, 해줄 처리들
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
