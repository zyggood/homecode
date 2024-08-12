package com.youcheng.demo.controller;

import com.youcheng.demo.aspect.RoleRequired;
import com.youcheng.demo.controller.vo.AddUserVo;
import com.youcheng.demo.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class UserController {


    @Resource
    private UserService userService;

    @PostMapping("/admin/addUser")
    @RoleRequired("admin")
    public ResponseEntity<?> addUserAccess(@RequestBody AddUserVo body, HttpServletRequest request) throws IOException {

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