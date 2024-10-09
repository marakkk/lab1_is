package org.marakobz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Setter;

@Data
@Embeddable
public class Coordinates {

    @Setter
    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    private double y;

}
