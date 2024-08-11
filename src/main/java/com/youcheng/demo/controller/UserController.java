package com.youcheng.demo.controller;

import com.youcheng.demo.controller.vo.AddUserVo;
import com.youcheng.demo.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {


    @Resource
    private UserService userService;

    @PostMapping("/admin/addUser")
    public ResponseEntity<?> addUserAccess(@RequestBody AddUserVo body, HttpServletRequest request) throws IOException {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access to this endpoint.");
        }

        userService.addUserAccess(body);

        return ResponseEntity.ok("Access granted to user " + body.getUserId());
    }

    @GetMapping("/user/{resource}")
    public ResponseEntity<?> checkUserAccess(@PathVariable String resource, HttpServletRequest request) throws IOException {
        String userId = (String) request.getAttribute("userId");

        if (userService.checkUserAccess(userId, resource)) {
            return ResponseEntity.ok("Access granted to resource " + resource);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied to resource " + resource);
        }
    }
}