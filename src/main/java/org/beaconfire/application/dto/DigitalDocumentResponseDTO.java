package org.beaconfire.application.dto;

import javax.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalDocumentResponseDTO {
    private Long id;
    private String title;
    private String type;
    private String path;
    private String description;
    private boolean required;

}
