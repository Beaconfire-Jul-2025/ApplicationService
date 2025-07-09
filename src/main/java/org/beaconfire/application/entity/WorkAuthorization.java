package org.beaconfire.application.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


@Entity
@Table(name = "work_authorization")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkAuthorization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Type: H1-B, L2, F1(CPT/OPT), H4, Other
    @Column(nullable = false)
    private String type;

    // Optional: If type == "Other"
    private String otherType;

    private LocalDate startDate;

    private LocalDate expirationDate;

    private String documentUrl;
}
