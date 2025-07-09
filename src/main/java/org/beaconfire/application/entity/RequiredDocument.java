package org.beaconfire.application.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "required_document")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequiredDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String documentType;

    private String s3Url;
}
