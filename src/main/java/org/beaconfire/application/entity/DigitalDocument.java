package org.beaconfire.application.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "DigitalDocument")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String type;

    private String path;

    private String description;

    @Column(nullable = false)
    private Boolean isRequired;
}
