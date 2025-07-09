package org.beaconfire.application.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalDocumentDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String type;

    @NotBlank
    private String path;

    private String description;

    @NotNull
    private Boolean isRequired;
}

