package io.synergy.minichat.controller;

import io.synergy.minichat.dto.UserResponseDto;
import io.synergy.minichat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/service/user")
    public UserResponseDto generateRandomUser() {
        return userService.findAll();
    }
}
