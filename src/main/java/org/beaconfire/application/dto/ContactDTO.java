package org.beaconfire.application.dto;

import javax.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactDTO {

    @NotBlank
    private String id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String cellPhone;

    private String alternatePhone;

    @Email
    @NotNull
    private String email;

    @NotNull
    private AddressDTO address;

    @NotNull
    private String relationship;

    @NotNull
    private String type;  // EMERGENCY or REFERENCE
}
