package org.marakobz.controller;

import org.marakobz.model.User;
import org.marakobz.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody User user) {
        logger.info("Registering user: {}", user.getUsername()); // Proper logging
        try {
            logger.info(user.toString());
            authService.register(user);
            logger.info("User {} registered successfully.", user.getUsername());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        logger.info("Attempting login for user: {}", user.getUsername());
        String token = authService.login(user.getUsername(), user.getPassword(), user.isAdmin());

        if (token != null) {
            logger.info("User {} logged in successfully.", user.getUsername());
            User loggedInUser = authService.getUserByUsername(user.getUsername());

            // Формируем ответ в формате JSON
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("isApproved", loggedInUser.isApproved());

            return ResponseEntity.ok(responseBody);
        } else {
            logger.warn("Failed login attempt for user: {}", user.getUsername());
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

}
