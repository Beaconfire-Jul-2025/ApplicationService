package org.beaconfire.application.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "reference")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String relationship;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "addressLine1", column = @Column(name = "address_line1")),
        @AttributeOverride(name = "addressLine2", column = @Column(name = "address_line2")),
        @AttributeOverride(name = "city", column = @Column(name = "city")),
        @AttributeOverride(name = "state", column = @Column(name = "state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "zip_code"))
    })
    private Address address;
}
