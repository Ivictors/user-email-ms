package com.victor.user.controller;

import com.victor.user.dto.UserDto;
import com.victor.user.model.UserModel;
import com.victor.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{uuid}")
    public ResponseEntity<UserModel> findById(@PathVariable UUID uuid){
        var user = userService.findById(uuid);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    public ResponseEntity<Void> save(@RequestBody UserDto userDto){
        userService.save(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
