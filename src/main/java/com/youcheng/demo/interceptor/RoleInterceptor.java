package com.youcheng.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String credentials = new String(Base64.getDecoder().decode(authHeader));
        // format userId:accountName:role
        String[] parts = credentials.split(":");

        if (parts.length != 3) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }

        request.setAttribute("userId", parts[0]);
        request.setAttribute("accountName", parts[1]);
        request.setAttribute("role", parts[2]);

        return true;
    }
}

