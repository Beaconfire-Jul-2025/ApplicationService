package org.beaconfire.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DigitalDocument")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Type is required")
    @Size(max = 100, message = "Type cannot exceed 100 characters")
    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "isRequired")
    @Builder.Default
    private Boolean isRequired = false;

    @NotBlank(message = "Path is required")
    @Size(max = 500, message = "Path cannot exceed 500 characters")
    @Column(name = "path", nullable = false, length = 500)
    private String path;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Size(max = 255, message = "Title cannot exceed 255 characters")
    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "createDate", updatable = false)
    private LocalDateTime createDate;

    @Column(name = "lastModificationDate")
    private LocalDateTime lastModificationDate;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
        lastModificationDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModificationDate = LocalDateTime.now();
    }
}