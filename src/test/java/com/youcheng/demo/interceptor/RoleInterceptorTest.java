package com.youcheng.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RoleInterceptorTest {

    private RoleInterceptor roleInterceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Object handler;

    @BeforeEach
    void setUp() {
        roleInterceptor = new RoleInterceptor();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        handler = new Object();
    }

    @Test
    void testPreHandle_WithValidAuthorizationHeader() throws Exception {
        String authHeader = Base64.getEncoder().encodeToString("123456:zyc:admin".getBytes());
        when(request.getHeader("Authorization")).thenReturn(authHeader);

        boolean result = roleInterceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request).setAttribute("userId", "123456");
        verify(request).setAttribute("accountName", "zyc");
        verify(request).setAttribute("role", "admin");
        verify(response, never()).setStatus(anyInt());
    }

    @Test
    void testPreHandle_WithMissingAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        boolean result = roleInterceptor.preHandle(request, response, handler);

        // Assert
        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void testPreHandle_WithInvalidAuthorizationHeader() throws Exception {
        String invalidAuthHeader = Base64.getEncoder().encodeToString("invalid:header".getBytes());
        when(request.getHeader("Authorization")).thenReturn(invalidAuthHeader);

        boolean result = roleInterceptor.preHandle(request, response, handler);

        // Assert
        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testPreHandle_WithBadlyFormattedAuthorizationHeader() throws Exception {
        String badlyFormattedHeader = Base64.getEncoder().encodeToString("badly:formatted:header:extra".getBytes());
        when(request.getHeader("Authorization")).thenReturn(badlyFormattedHeader);

        boolean result = roleInterceptor.preHandle(request, response, handler);

        // Assert
        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
