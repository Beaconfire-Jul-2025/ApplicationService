package org.beaconfire.application.service.impl;

import org.beaconfire.application.dto.ApplicationWorkflowRequestDTO;
import org.beaconfire.application.entity.ApplicationWorkFlow;
import org.beaconfire.application.repository.ApplicationWorkflowRepository;
import org.beaconfire.application.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ApplicationWorkflowServiceImplTest {

    @Mock
    private ApplicationWorkflowRepository applicationWorkflowRepository;

    @InjectMocks
    private ApplicationWorkflowServiceImpl applicationWorkflowService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Success Setup
    @Test
    void testCreateApplicationWorkflow_Success() {
        ApplicationWorkflowRequestDTO dto = ApplicationWorkflowRequestDTO.builder()
                .employeeId("EMP001")
                .applicationType("Onboarding")
                .build();

        ApplicationWorkFlow saved = ApplicationWorkFlow.builder()
                .id(1L)
                .employeeId("EMP001")
                .applicationType("Onboarding")
                .status("Pending")
                .build();

        when(applicationWorkflowRepository.save(any())).thenReturn(saved);

        ApplicationWorkFlow result = applicationWorkflowService.createApplicationWorkflow(dto);

        assertNotNull(result);
        assertEquals("EMP001", result.getEmployeeId());
        assertEquals("Onboarding", result.getApplicationType());
        assertEquals("Pending", result.getStatus());
    }

    // Success: Search
    @Test
    void testFindByEmployeeIdAndApplicationType_Found() {
        ApplicationWorkFlow wf = ApplicationWorkFlow.builder()
                .id(10L)
                .employeeId("EMP888")
                .applicationType("Onboarding")
                .status("Pending")
                .build();

        when(applicationWorkflowRepository.findByEmployeeId("EMP888"))
                .thenReturn(Optional.of(wf));

        Optional<ApplicationWorkFlow> result = applicationWorkflowService.findByEmployeeId("EMP888");

        assertTrue(result.isPresent());
        assertEquals("EMP888", result.get().getEmployeeId());
    }

    // Failure: Empty employeeId
    @Test
    void testCreateApplicationWorkflow_BlankEmployeeId_ShouldFail() {
        ApplicationWorkflowRequestDTO dto = ApplicationWorkflowRequestDTO.builder()
                .employeeId("")
                .applicationType("Onboarding")
                .build();

        when(applicationWorkflowRepository.save(any()))
                .thenThrow(new IllegalArgumentException("Employee ID cannot be blank"));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            applicationWorkflowService.createApplicationWorkflow(dto);
        });

        assertEquals("Employee ID cannot be blank", ex.getMessage());
    }

    // Failure: EmployeeId too long
    @Test
    void testCreateApplicationWorkflow_LongEmployeeId_ShouldFail() {
        String longId = "EMP_" + repeat("X", 1000); // very long
        ApplicationWorkflowRequestDTO dto = ApplicationWorkflowRequestDTO.builder()
            .employeeId(longId)
            .applicationType("Onboarding")
            .build();

        when(applicationWorkflowRepository.save(any()))
            .thenThrow(new IllegalArgumentException("Employee ID is too long"));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            applicationWorkflowService.createApplicationWorkflow(dto);
        });

        assertTrue(ex.getMessage().contains("too long"));
    }

    private String repeat(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    // Failure: Null DTO
    @Test
    void testCreateApplicationWorkflow_NullDTO_ShouldFail() {
        assertThrows(NullPointerException.class, () -> {
            applicationWorkflowService.createApplicationWorkflow(null);
        });
    }

    // Failure: Application exists, should throw Exception
    @Test
    void createApplicationWorkflow_shouldThrowIfEmployeeIdExists() {
        ApplicationWorkflowRequestDTO dto = ApplicationWorkflowRequestDTO.builder()
            .employeeId("EMP001")
            .applicationType("Onboarding")
            .build();

        ApplicationWorkFlow existing = ApplicationWorkFlow.builder()
            .id(1L)
            .employeeId("EMP001")
            .applicationType("StatusChange")
            .status("Pending")
            .build();

        when(applicationWorkflowRepository.findByEmployeeId("EMP001"))
            .thenReturn(Optional.of(existing));

        Exception ex = assertThrows(ApplicationAlreadyExistsException.class, () -> {
            applicationWorkflowService.createApplicationWorkflow(dto);
        });

        assertEquals("Application already exists for employee: EMP001", ex.getMessage());
    }

    // Success: found Application List
    @Test
    void getApplicationsByStatus_shouldReturnList_whenFound() {
        List<ApplicationWorkFlow> pendingList = Arrays.asList(
                ApplicationWorkFlow.builder()
                .id(1L)
                .employeeId("EMP001")
                .applicationType("Onboarding")
                .status("Pending")
                .build(),
                ApplicationWorkFlow.builder()
                .id(2L)
                .employeeId("EMP002")
                .applicationType("StatusChange")
                .status("Pending")
                .build()
                );

        when(applicationWorkflowRepository.findByStatus("Pending"))
            .thenReturn(pendingList);

        List<ApplicationWorkFlow> result = applicationWorkflowService.getApplicationsByStatus("Pending");

        assertEquals(2, result.size());
        assertEquals("EMP001", result.get(0).getEmployeeId());
        assertEquals("Pending", result.get(0).getStatus());
    }

    // Success: found empty Application List
    @Test
    void getApplicationsByStatus_shouldReturnEmptyList_whenNoneFound() {
        when(applicationWorkflowRepository.findByStatus("Approved"))
            .thenReturn(Collections.emptyList());

        List<ApplicationWorkFlow> result = applicationWorkflowService.getApplicationsByStatus("Approved");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Success: update application status
    @Test
    void updateApplicationStatus_shouldUpdateSuccessfully() {
        Long id = 1L;
        ApplicationWorkFlow existingApp = ApplicationWorkFlow.builder()
            .id(id)
            .employeeId("EMP001")
            .applicationType("Onboarding")
            .status("Pending")
            .comment(null)
            .build();

        when(applicationWorkflowRepository.findById(id)).thenReturn(Optional.of(existingApp));

        applicationWorkflowService.updateApplicationStatus(id, "Complete", "Approved by HR");

        assertEquals("Complete", existingApp.getStatus());
        assertEquals("Approved by HR", existingApp.getComment());
        verify(applicationWorkflowRepository, times(1)).save(existingApp);
    }

    // Failure: Wrong application ID
    @Test
    void updateApplicationStatus_shouldThrowException_whenApplicationNotFound() {
        Long id = 999L;

        when(applicationWorkflowRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApplicationNotFoundException.class, () -> {
            applicationWorkflowService.updateApplicationStatus(id, "Rejected", "Not eligible");
        });

        verify(applicationWorkflowRepository, never()).save(any());
    }
}
