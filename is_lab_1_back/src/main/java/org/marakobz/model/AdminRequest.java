package org.marakobz.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admin_request")
public class AdminRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // The user who is requesting admin rights.

    @Getter
    @Setter
    @Column(name = "status")
    private String status = "PENDING";  // Possible values: PENDING, APPROVED, REJECTED
}
