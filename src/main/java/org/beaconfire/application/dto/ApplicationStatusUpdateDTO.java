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
    @Pattern(regexp = "Complete|Rejected", message = "Status must be 'Complete' or 'Rejected'")
    private String status;

    private String comment;
}
