package org.marakobz.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "AppUser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String passwordHash;

    private String password;

    private boolean isAdmin;

}
