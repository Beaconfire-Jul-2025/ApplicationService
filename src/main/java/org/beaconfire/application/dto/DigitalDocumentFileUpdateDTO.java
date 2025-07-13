package org.beaconfire.application.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalDocumentFileUpdateDTO {
    @NotBlank(message = "Path is required")
    private String path;
}
