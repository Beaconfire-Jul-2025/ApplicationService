package org.beaconfire.application.entity;

import javax.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "ApplicationWorkFlow")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationWorkFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK to Employee, ID
    @Column(nullable = false)
    private String employeeId;

    @Column(nullable = false)
    private String applicationType;

    @Column(nullable = false)
    private String status;

    private String comment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(nullable = false)
    private LocalDateTime lastModificationDate;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
        lastModificationDate = createDate;
    }

    @PreUpdate
    protected void onUpdate() {
        lastModificationDate = LocalDateTime.now();
    }
}
