package com.youcheng.demo.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class RoleAspect {

    @Before("@annotation(com.youcheng.demo.aspect.RoleRequired)")
    public void checkRole(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取注解中的角色要求
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RoleRequired roleRequired = signature.getMethod().getAnnotation(RoleRequired.class);

        String requiredRole = roleRequired.value();
        String role = (String) request.getAttribute("role");

        //check role
        if (!requiredRole.equals(role)) {
            throw new RuntimeException("You do not have access to this endpoint.");
        }
    }
}
