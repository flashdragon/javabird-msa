package com.example.user_service.security;

import com.example.user_service.dto.UserDto;
import com.example.user_service.service.UserService;
import com.example.user_service.vo.RequestLogin;
import com.example.user_service.vo.ResponseLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

import static com.example.user_service.utils.ApiUtils.error;
import static com.example.user_service.utils.ApiUtils.success;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;
    private final Environment env;
    private final byte[] secretKey;

    public final static Long TOKEN_EXPIRE_COUNT = 1 * 24 * 60 * 60 * 1000L;// 1 days

    public AuthenticationFilter (UserService userService, Environment env) {
        this.userService = userService;
        this.env = env;
        this.secretKey = env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
            String username = ((User)authResult.getPrincipal()).getUsername();
            UserDto userDetails = userService.getUserDetailsByEmail(username);

            Claims claims = Jwts.claims();
            claims.put("name", userDetails.getName());
            claims.put("id", userDetails.getUserId());

            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userDetails.getUserId())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + TOKEN_EXPIRE_COUNT))
                    .signWith(getSigningKey(secretKey))
                    .compact();

            response.addHeader("token", token);
            response.addHeader("userId", userDetails.getUserId());

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getOutputStream(), success(new ResponseLogin(token, userDetails.getUserId())));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), error(new UsernameNotFoundException("User not found."), HttpStatus.UNAUTHORIZED));
    }

    public static Key getSigningKey(byte[] secretKey) {
        return Keys.hmacShaKeyFor(secretKey);
    }

}
