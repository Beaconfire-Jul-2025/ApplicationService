package org.beaconfire.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyContactDTO {

    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String phone;

    @NotBlank
    private String email;

    @NotBlank
    private String relationship;
}
