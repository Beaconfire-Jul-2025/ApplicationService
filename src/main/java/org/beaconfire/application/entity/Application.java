package org.beaconfire.application.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Entity representing an onboarding application for a new employee.
 */
@Entity
@Table(name = "application")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user ID from the authentication service (used to link employee)
    @Column(nullable = false, unique = true)
    private String userId;

    // Basic information
    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @Column(nullable = false)
    private String lastName;

    private String preferredName;

    private String avatarUrl;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String cellPhone;

    private String alternatePhone;

    @Column(nullable = false)
    private String ssn;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String gender;

    // Embedded current address
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "addressLine1", column = @Column(name = "current_address_line1")),
        @AttributeOverride(name = "addressLine2", column = @Column(name = "current_address_line2")),
        @AttributeOverride(name = "city", column = @Column(name = "current_city")),
        @AttributeOverride(name = "state", column = @Column(name = "current_state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "current_zip_code"))
    })
    private Address currentAddress;

    // Citizenship & Work Authorization
    @Column(nullable = false)
    private Boolean usCitizenOrPermanentResident;

    // Only for US citizens or green card holders
    private String citizenshipStatus;

    // Only for non-citizens
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "work_auth_id")
    private WorkAuthorization workAuthorization;

    // Driver license section (conditionally required)
    @Column(nullable = false)
    private Boolean hasDriverLicense;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_license_id")
    private DriverLicense driverLicense;

    // Reference person (one only)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reference_id")
    private Reference reference;

    // List of emergency contacts
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "application_id")
    private List<EmergencyContact> emergencyContacts;

    // List of required documents uploaded by the user
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "application_id")
    private List<RequiredDocument> requiredDocuments;
}
