package org.beaconfire.application.dto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnboardingSubmitDTO {

    // FK Employee ID
    @NotBlank
    private String id;

    @NotBlank
    private String userId;

    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

    private String preferredName;

    @Email
    private String email;

    @NotBlank
    private String cellPhone;

    private String alternatePhone;

    private String gender;

    @NotBlank
    private String ssn;

    @NotNull
    private LocalDate dob;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private @Valid DriverLicenseDTO driverLicense;

    private LocalDate driverLicenseExpiration;

    private String houseId;

    private @Valid ContactDTO referenceContact;

    private List<@Valid ContactDTO> emergencyContact;

    @NotNull
    private List<@Valid AddressDTO> currentAddress;

    @NotNull
    private List<@Valid VisaStatusDTO> visaStatus;

    @NotNull
    private List<@Valid PersonalDocumentDTO> personalDocument;

    @NotBlank
    private String applicationType;
}
