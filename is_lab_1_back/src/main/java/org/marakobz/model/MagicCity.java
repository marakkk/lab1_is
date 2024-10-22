package org.marakobz.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class MagicCity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private float area;

    @Column
    private long population;

    @Column
    private LocalDate establishmentDate;

    @Column
    private BookCreatureType governor;

    @Column
    private boolean capital;

    @Column
    private Float populationDensity;


}
