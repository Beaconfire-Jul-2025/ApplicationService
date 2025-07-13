package org.beaconfire.application.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalDocumentUpdateDTO {

    @NotBlank
    private String type;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private boolean required;
}
