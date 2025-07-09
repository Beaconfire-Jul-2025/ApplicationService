package org.beaconfire.application.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


@Entity
@Table(name = "driver_license")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String licenseNumber;
    private LocalDate expirationDate;
    private String documentUrl;
}
