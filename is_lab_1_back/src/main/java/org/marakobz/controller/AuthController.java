package org.marakobz.controller;

import org.marakobz.model.User;
import org.marakobz.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody User user) {
        authService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String token = authService.login(user.getUsername(), user.getPassword());
        if (token != null) {
            return ResponseEntity.ok(token);
        }
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }
}
