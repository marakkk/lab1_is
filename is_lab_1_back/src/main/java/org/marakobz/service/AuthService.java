package org.marakobz.service;

import jakarta.persistence.NoResultException;
import org.marakobz.controller.AuthController;
import org.marakobz.model.User;
import org.marakobz.repository.UserRepository;
import org.marakobz.security.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void register(User user) {
        logger.info("Registering user: {}", user.getUsername());
        user.setPasswordHash(JWTUtil.hashPassword(user.getPassword())); // Hash password
        logger.info("Is Admin: {}", user.isAdmin()); // Log if the user is admin
        userRepository.save(user); // Save the user with the correct isAdmin value
        logger.info("Is Admin: {}", user.isAdmin());
    }


    public String login(String username, String password, boolean admin) {
        try {
            User user = userRepository.findByUsername(username);

            if (user != null && JWTUtil.verifyPassword(password, user.getPasswordHash())) {
                logger.info("User logged in: {}", username);
                return JWTUtil.generateToken(user);
            }
        } catch (NoResultException e) {
            logger.error("User not found: {}", username);
        }

        return null;
    }


    @Transactional
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Transactional(readOnly = true)
    public List<User> findAllAdminUsers() {
        return userRepository.findByIsAdminTrue();
    }

    @Transactional
    public void approve(User user) {
        userRepository.save(user); // Save the user with the correct isAdmin value

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}