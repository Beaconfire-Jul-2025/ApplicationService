package org.beaconfire.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationWorkflowRequestDTO {

    @NotNull
    private Long employeeId;

    @NotBlank
    private String applicationType;

    @NotBlank
    private String status;
}
