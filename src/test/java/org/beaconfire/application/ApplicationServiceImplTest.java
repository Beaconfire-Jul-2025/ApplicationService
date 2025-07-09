package org.beaconfire.application.service;

import org.beaconfire.application.dto.*;
import org.beaconfire.application.entity.Application;
import org.beaconfire.application.exception.ApplicationAlreadyExistsException;
import org.beaconfire.application.repository.ApplicationRepository;
import org.beaconfire.application.service.impl.ApplicationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ApplicationServiceImpl.
 */
public class ApplicationServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test saving a valid application should call repository and return the saved entity.
     */
    @Test
    public void testSaveApplication_Success() {
        // Given
        ApplicationRequestDTO dto = getValidDto();
        when(applicationRepository.findByUserId("test-user")).thenReturn(Optional.empty());
        when(applicationRepository.save(any(Application.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        Application savedApp = applicationService.saveApplication(dto);

        // Then
        assertEquals("test-user", savedApp.getUserId());
        assertEquals("John", savedApp.getFirstName());
        assertEquals("Doe", savedApp.getLastName());
        assertNotNull(savedApp.getReference());
        verify(applicationRepository, times(1)).save(any(Application.class));
    }

    /**
     * Test saving an application when userId already exists should throw exception.
     */
    @Test
    public void testSaveApplication_DuplicateUserId() {
        // Given
        ApplicationRequestDTO dto = getValidDto();
        when(applicationRepository.findByUserId("test-user")).thenReturn(Optional.of(new Application()));

        // When / Then
        assertThrows(ApplicationAlreadyExistsException.class, () -> {
            applicationService.saveApplication(dto);
        });

        verify(applicationRepository, never()).save(any(Application.class));
    }

    // Utility method to create a valid DTO
    private ApplicationRequestDTO getValidDto() {
        return ApplicationRequestDTO.builder()
                .userID("test-user")
                .firstName("John")
                .lastName("Doe")
                .ssn("XXX-XX-1234")
                .email("john.doe@example.com")
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .gender("Male")
                .currentAddress(AddressDTO.builder()
                        .addressLine1("123 Main St")
                        .city("Seattle")
                        .state("WA")
                        .zipCode("98101")
                        .build())
                .usCitizenOrPermanentResident(true)
                .hasDriverLicense(false)
                .reference(ReferenceDTO.builder()
                        .firstName("Jane")
                        .lastName("Smith")
                        .phone("123-456-7890")
                        .email("jane@example.com")
                        .relationship("Friend")
                        .address(AddressDTO.builder()
                                .addressLine1("456 Oak Ave")
                                .city("Bellevue")
                                .state("WA")
                                .zipCode("98004")
                                .build())
                        .build())
                .emergencyContacts(Collections.singletonList(EmergencyContactDTO.builder()
                        .firstName("Emily")
                        .lastName("Brown")
                        .phone("111-222-3333")
                        .email("emily@example.com")
                        .relationship("Spouse")
                        .build()))
                .requiredDocuments(Collections.emptyList())
                .build();
    }
}
