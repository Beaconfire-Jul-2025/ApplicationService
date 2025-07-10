package org.beaconfire.application.dto;

import lombok.*;
import javax.validation.constraints.*;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisaStatusDTO {

    @NotBlank
    private String id;

    @NotBlank
    private String visaType;

    @NotNull
    private Boolean activeFlag;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime lastModificationDate;
}
