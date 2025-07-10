package org.beaconfire.application.dto;

import lombok.*;
import javax.validation.constraints.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalDocumentDTO {

    @NotBlank
    private String id;

    @NotBlank
    private String path;

    @NotBlank
    private String title;

    private String comment;

    @NotNull
    private LocalDateTime createDate;
}
