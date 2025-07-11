package org.beaconfire.application.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationStatusUpdateDTO {

    @NotBlank
    @Pattern(regexp = "PENDING|IN_PROGRESS|COMPLETED|REJECTED|CANCELLED", message = "Status error")
    private String status;

    private String comment;
}
