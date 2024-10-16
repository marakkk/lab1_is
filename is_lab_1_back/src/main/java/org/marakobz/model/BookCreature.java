package org.marakobz.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import lombok.Data;


@Data
@Entity
public class BookCreature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Coordinates coordinates;

    @Column(nullable = false)
    private ZonedDateTime creationDate;

    @Column
    private Long age;

    @Enumerated(EnumType.STRING)
    private BookCreatureType creatureType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creature_location_id")
    private MagicCity creatureLocation;

    @Column(nullable = false)
    private Float attackLevel;

    @Column(nullable = false)
    private double defenseLevel;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ring_id")
    private Ring ring;


}