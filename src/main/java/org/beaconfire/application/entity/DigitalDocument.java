package org.beaconfire.application.entity;

import javax.persistence.*;
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

    @Column(nullable = false)
    private String path;

    private String description;

    @Column(name = "isRequired", nullable = false)
    private boolean required;
}
