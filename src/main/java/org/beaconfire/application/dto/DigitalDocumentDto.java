package org.beaconfire.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalDocumentDto {

    private Integer id;

    @NotBlank(message = "Type is required")
    @Size(max = 100, message = "Type cannot exceed 100 characters")
    private String type;

    private Boolean isRequired;

    @NotBlank(message = "Path is required")
    @Size(max = 500, message = "Path cannot exceed 500 characters")
    private String path;

    private String description;

    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModificationDate;
}
