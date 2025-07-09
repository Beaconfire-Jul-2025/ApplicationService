package org.beaconfire.application.service.impl;

import org.beaconfire.application.dto.*;
import org.beaconfire.application.entity.*;
import org.beaconfire.application.exception.ApplicationAlreadyExistsException;
import org.beaconfire.application.repository.ApplicationRepository;
import org.beaconfire.application.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Application saveApplication(ApplicationRequestDTO dto) {

        // Check if Application Exist
        if (applicationRepository.findByUserId(dto.getUserID()).isPresent()) {
            throw new ApplicationAlreadyExistsException(dto.getUserID());
        }

        // Convert DTO to Entity
        Application application = Application.builder()
                .userId(dto.getUserID())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .preferredName(dto.getPreferredName())
                .avatarUrl(dto.getAvatarUrl())
                .email(dto.getEmail())
                .cellPhone(dto.getCellPhone())
                .alternatePhone(dto.getAlternatePhone())
                .ssn(dto.getSsn())
                .dateOfBirth(dto.getDateOfBirth())
                .gender(dto.getGender())
                .currentAddress(mapAddress(dto.getCurrentAddress()))
                .usCitizenOrPermanentResident(dto.getUsCitizenOrPermanentResident())
                .citizenshipStatus(dto.getCitizenshipStatus())
                .workAuthorization(dto.getWorkAuthorization() != null ? mapWorkAuth(dto.getWorkAuthorization()) : null)
                .hasDriverLicense(dto.getHasDriverLicense())
                .driverLicense(dto.getDriverLicense() != null ? mapDriverLicense(dto.getDriverLicense()) : null)
                .reference(mapReference(dto.getReference()))
                .emergencyContacts(dto.getEmergencyContacts().stream()
                        .map(this::mapEmergencyContact).collect(Collectors.toList()))
                .requiredDocuments(dto.getRequiredDocuments() != null ? dto.getRequiredDocuments().stream()
                                .map(this::mapRequiredDocument).collect(Collectors.toList()) : null)
                .build();

        return applicationRepository.save(application);
    }

    private Address mapAddress(AddressDTO dto) {
        return Address.builder()
                .addressLine1(dto.getAddressLine1())
                .addressLine2(dto.getAddressLine2())
                .city(dto.getCity())
                .state(dto.getState())
                .zipCode(dto.getZipCode())
                .build();
    }

    private WorkAuthorization mapWorkAuth(WorkAuthorizationDTO dto) {
        return WorkAuthorization.builder()
                .type(dto.getType())
                .otherType(dto.getOtherType())
                .startDate(dto.getStartDate())
                .expirationDate(dto.getExpirationDate())
                .documentUrl(dto.getDocumentUrl())
                .build();
    }

    private DriverLicense mapDriverLicense(DriverLicenseDTO dto) {
        return DriverLicense.builder()
                .licenseNumber(dto.getLicenseNumber())
                .expirationDate(dto.getExpirationDate())
                .documentUrl(dto.getDocumentUrl())
                .build();
    }

    private Reference mapReference(ReferenceDTO dto) {
        return Reference.builder()
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .relationship(dto.getRelationship())
                .address(mapAddress(dto.getAddress()))
                .build();
    }

    private EmergencyContact mapEmergencyContact(EmergencyContactDTO dto) {
        return EmergencyContact.builder()
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .relationship(dto.getRelationship())
                .build();
    }

    private RequiredDocument mapRequiredDocument(RequiredDocumentDTO dto) {
        return RequiredDocument.builder()
                .documentType(dto.getDocumentType())
                .s3Url(dto.getS3Url())
                .build();
    }
}
