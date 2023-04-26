package com.example.userservice.config;

import com.example.userservice.controller.request.RequestLogin;
import com.example.userservice.model.user.UserDto;
import com.example.userservice.model.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;
    private final Environment env;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // Register into RequestLogin format, data received from request
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            // 인증 정보를 만들어 보자,
            // 사용자가 입력했던 이메일, 비밀번호를 UsernamePasswordAuthenticationToken 으로 바꿔서 getAuthenticationManager() 인증 처리해주는 manager 에 넘겨서 아이디와 비밀번호를 비교해준다
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 로그인 성공했을 떄, 정확하게 어떻게 해줄것인지, 예를 들어, 토큰 타임, 반환 값
        // 여기 아래있는 User 는 spring security 에서 제공하는 User,
        // attemptAuthentication -> UserServiceImpl.loadUserByUsername -> successfulAuthentication
        log.debug(((User)authResult.getPrincipal()).getUsername());

        String username = ((User) authResult.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(username);
    }
}
