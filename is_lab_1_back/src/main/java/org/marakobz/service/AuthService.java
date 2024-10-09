package org.marakobz.service;

import jakarta.persistence.NoResultException;
import org.marakobz.model.User;
import org.marakobz.repository.UserRepository;
import org.marakobz.security.JWTUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void register(User user) {
        System.out.println("Registering user: " + user.getUsername());
        user.setPasswordHash(JWTUtil.hashPassword(user.getPassword()));
        userRepository.save(user);
    }

    public String login(String username, String password) {
        try {
            User user = userRepository.findByUsername(username);

            if (user != null && JWTUtil.verifyPassword(password, user.getPasswordHash())) {
                System.out.println("User logged in: " + username);
                return JWTUtil.generateToken(user);
            }
        } catch (NoResultException e) {
            System.out.println("User not found: " + username);
        }

        return null;
    }
}