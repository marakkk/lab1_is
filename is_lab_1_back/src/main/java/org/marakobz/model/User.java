package org.marakobz.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "app_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter // Added Setter to allow modification if needed
    @Column(unique = true)
    private String username;

    @Getter
    @Setter
    private String passwordHash;

    @Getter
    private String password; // Consider removing this if you don't need it for persistence


    @Column(name = "is_admin")
    private boolean isAdmin; // No default value; it will be set from the frontend

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    public boolean isAdmin() {
        return isAdmin;
    }




    @Column(name = "is_approved")
    private boolean isApproved; // Default value can be set in the constructor


    public void setIsApproved(boolean isApproved)  {
        this.isApproved = true;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public User() {
        this.isAdmin = false; // Set default value for isAdmin here
        this.isApproved = false; // Set default value for isApproved here
    }

    public Long getId() {
        return id;
    }



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", isAdmin=" + isAdmin +
                ", isApproved=" + isApproved +
                '}';
    }


}
