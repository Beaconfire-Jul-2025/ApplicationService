package org.beaconfire.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverLicenseDTO {

    private String licenseNumber;
    private LocalDate expirationDate;
    private String documentUrl;
}

