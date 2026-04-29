package com.myblog.controller;

import com.myblog.common.Result;
import com.myblog.entity.User;
import com.myblog.service.UserService;
import com.myblog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginForm) {
        String username = loginForm.get("username");
        String password = loginForm.get("password");

        if (username == null || password == null) {
            return Result.error("用户名或密码不能为空");
        }

        User user = userService.login(username, password);

        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        // 生成JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);

        return Result.success(result);
    }

    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.unauthorized();
        }

        String actualToken = token.substring(7);
        if (!jwtUtil.validateToken(actualToken)) {
            return Result.unauthorized();
        }

        String username = jwtUtil.getUsernameFromToken(actualToken);
        User user = userService.getByUsername(username);

        if (user == null) {
            return Result.unauthorized();
        }

        user.setPassword(null);
        return Result.success(user);
    }
}