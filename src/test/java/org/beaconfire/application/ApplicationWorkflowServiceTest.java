package org.beaconfire.application;

import org.beaconfire.application.entity.ApplicationWorkFlow;
import org.beaconfire.application.repository.ApplicationWorkflowRepository;
import org.beaconfire.application.service.impl.ApplicationWorkflowServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ApplicationWorkflowServiceTest {

    private ApplicationWorkflowRepository repository;
    private ApplicationWorkflowServiceImpl service;

    @BeforeEach
    public void setup() {
        repository = Mockito.mock(ApplicationWorkflowRepository.class);
        service = new ApplicationWorkflowServiceImpl(repository);
    }

    @Test
    public void testSaveNewApplicationWorkflow_Success() {
        String employeeId = "emp_001";
        String applicationType = "ONBOARDING";
        String comment = "Initial submission";

        // no existing workflow
        when(repository.findByEmployeeId(employeeId)).thenReturn(Optional.empty());

        when(repository.save(any(ApplicationWorkFlow.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ApplicationWorkFlow saved = service.saveApplicationWorkflow(employeeId, applicationType, comment);

        assertEquals(employeeId, saved.getEmployeeId());
        assertEquals(applicationType, saved.getApplicationType());
        assertEquals("PENDING", saved.getStatus());
        assertEquals(comment, saved.getComment());

        verify(repository, times(1)).save(any(ApplicationWorkFlow.class));
    }

    @Test
    public void testSaveApplicationWorkflow_DuplicateError() {
        String employeeId = "emp_001";
        String applicationType = "ONBOARDING";

        ApplicationWorkFlow existing = ApplicationWorkFlow.builder()
                .employeeId(employeeId)
                .applicationType(applicationType)
                .build();

        when(repository.findByEmployeeId(employeeId)).thenReturn(Optional.of(existing));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.saveApplicationWorkflow(employeeId, applicationType, "some comment");
        });

        assertTrue(exception.getMessage().contains("already exists"));
        verify(repository, never()).save(any());
    }
}
