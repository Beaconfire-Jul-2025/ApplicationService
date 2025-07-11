package org.beaconfire.application.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalDocumentRequestDTO {

    @NotBlank
    private String type;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private boolean required;

    @NotBlank
    private String path;
}
