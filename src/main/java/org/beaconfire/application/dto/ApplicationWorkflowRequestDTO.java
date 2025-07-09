package org.beaconfire.application.dto;

import javax.validation.constraints.*;
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

    private String comment;
}
