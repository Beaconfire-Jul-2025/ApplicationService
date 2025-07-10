package org.beaconfire.application.dto;

import javax.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

    @NotBlank
    private String ID;

    @NotBlank
    private String AddressLine1;

    private String AddressLine2;

    @NotBlank
    private String City;

    @NotBlank
    private String State;

    @NotBlank
    private String ZipCode;
}
