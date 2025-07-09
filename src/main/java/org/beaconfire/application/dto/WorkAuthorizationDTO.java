package org.beaconfire.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkAuthorizationDTO {

    @NotBlank
    private String type;

    private String otherType;
    private LocalDate startDate;
    private LocalDate expirationDate;
    private String documentUrl;
}
