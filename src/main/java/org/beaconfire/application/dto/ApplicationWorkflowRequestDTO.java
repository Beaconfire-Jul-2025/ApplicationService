package org.beaconfire.application.dto;

import javax.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationWorkflowRequestDTO {

    @NotBlank(message = "Employee ID cannot be blank")
    @Size(max = 100, message = "Employee ID is too long")
    private String employeeId;

    @NotBlank(message = "Application Type cannot be blank")
    @Pattern(regexp = "Onboarding|StatusChange", message = "Invalid application type")
    private String applicationType;
}

