package org.marakobz.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.marakobz.model.User;

import javax.crypto.SecretKey;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class JWTUtil {
    private static final long EXPIRATION_TIME = 86400000; // 1 day
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Generate JWT token using the user's information
    public static String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername()) // Username as subject
                .setIssuedAt(new Date())         // Issued at
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Token expiry time
                .signWith(SECRET_KEY)            // Signature algorithm and secret key
                .compact();
    }

    // Method to extract the username (subject) from the token
    public static String extractUsername(String token) {
        return getClaims(token).getSubject(); // Extract the "subject" field from the token, which is the username
    }

    // Helper method to extract username from request (Authorization header)
    public static String extractUsernameFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            return extractUsername(token);          // Extract the username from the token
        }
        return null;
    }

    // Method to validate the token
    public static boolean isTokenValid(String token, User user) {
        String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    // Check if the token has expired
    private static boolean isTokenExpired(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // Helper method to get the claims (payload) from the token
    private static Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)          // Specify the secret key
                .build()
                .parseClaimsJws(token)              // Parse the token
                .getBody();                         // Get the payload (claims)
    }

    // Verify the password by hashing the input password and comparing with the stored hashed password
    public static boolean verifyPassword(String password, String hashed) {
        String hashedPassword = hashPassword(password);
        return hashedPassword.equals(hashed);
    }

    // Hash the password using SHA-512
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 algorithm not available", e);
        }
    }
}