package org.beaconfire.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "ApplicationWorkFlow")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationWorkFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Employee ID is required")
    @Size(max = 100, message = "Employee ID cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String employeeId;

    @Column(updatable = false)
    private LocalDateTime createDate;

    private LocalDateTime lastModificationDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    @Builder.Default
    private WorkFlowStatus status = WorkFlowStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @NotBlank(message = "Application type is required")
    private String applicationType;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
        lastModificationDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModificationDate = LocalDateTime.now();
    }

    public enum WorkFlowStatus {
        OPEN, COMPLETED, REJECTED, PENDING
    }
}