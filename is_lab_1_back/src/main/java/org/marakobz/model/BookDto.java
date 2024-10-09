package org.marakobz.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class BookDto {

    private String name;

    private Coordinates coordinates;

    private Long age;

    @Enumerated(EnumType.STRING)
    private BookCreatureType creatureType;

    private MagicCity creatureLocation;

    private Float attackLevel;

    private double defenseLevel;

    private Ring ring;
}
