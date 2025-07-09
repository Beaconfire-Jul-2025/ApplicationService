package org.beaconfire.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequiredDocumentDTO {

    @NotBlank
    private String documentType;

    @NotBlank
    private String s3Url;
}
