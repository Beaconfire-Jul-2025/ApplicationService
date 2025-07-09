package org.beaconfire.application.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import lombok.*;
import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationRequestDTO {

    @NotBlank
    private String userID;

    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

    private String preferredName;

    private String avatarUrl;

    @Email
    @NotBlank
    private String email;

    private String cellPhone;

    private String alternatePhone;

    @NotBlank
    private String ssn;

    @NotNull
    private LocalDate dateOfBirth;

    @NotBlank
    private String gender;

    @NotNull
    private AddressDTO currentAddress;

    @NotNull
    private Boolean usCitizenOrPermanentResident;

    private String citizenshipStatus;

    private WorkAuthorizationDTO workAuthorization;

    @NotNull
    private Boolean hasDriverLicense;

    private DriverLicenseDTO driverLicense;

    @NotNull
    private ReferenceDTO reference;

    @NotEmpty
    private List<@Valid EmergencyContactDTO> emergencyContacts;

    private List<@Valid RequiredDocumentDTO> requiredDocuments;
}
