package org.marakobz.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Ring {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long power;

    @Column(nullable = false)
    private Float weight;

}
