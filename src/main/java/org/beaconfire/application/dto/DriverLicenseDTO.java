package org.beaconfire.application.dto;

import javax.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverLicenseDTO {

    @NotBlank
    private String licenseNumber;

    @NotNull
    private LocalDate expirationDate;
}

