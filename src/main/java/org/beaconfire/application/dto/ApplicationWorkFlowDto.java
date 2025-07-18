package org.beaconfire.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beaconfire.application.model.ApplicationWorkFlow;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationWorkFlowDto {

    private Integer id;

    @NotBlank(message = "Employee ID is required")
    @Size(max = 100, message = "Employee ID cannot exceed 100 characters")
    private String employeeId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModificationDate;

    private ApplicationWorkFlow.WorkFlowStatus status;

    private String comment;

    @NotBlank(message = "Application type is required")
    private String applicationType;
}
