package com.example.springsecurity.config;

import com.example.springsecurity.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomTokenFilter extends OncePerRequestFilter {

    private final UserService userService;

    public CustomTokenFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try{
            String accessToken, verifyToken;
            accessToken = getHeaderValue(httpServletRequest.getHeader("accessToken"));
            verifyToken = getHeaderValue(httpServletRequest.getHeader("verifyToken"));

            if (!StringUtils.hasText(accessToken) || !StringUtils.hasText(verifyToken)){
                errorResponse(httpServletRequest, httpServletResponse, HttpStatus.UNAUTHORIZED, "Unauthorized!!!");
                return;
            }

            if (!userService.validateToken(accessToken, verifyToken)){
                errorResponse(httpServletRequest, httpServletResponse, HttpStatus.FORBIDDEN, "Unauthorized!!!");
                return;
            }

            UserDetails userDetails = userService.loadUserByToken(accessToken);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, accessToken, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        }catch (Exception e){
            errorResponse(httpServletRequest, httpServletResponse, HttpStatus.UNAUTHORIZED, "Unauthorized!!!");
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getHeaderValue(String value){
        return StringUtils.hasText(value) ? value : "";
    }

    private static void errorResponse(HttpServletRequest request, HttpServletResponse response, HttpStatus httpStatus, String responseData) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(httpStatus.value());
        response.getWriter().print(responseData);
    }
}
