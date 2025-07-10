package org.beaconfire.application.service;

import org.beaconfire.application.dto.ApplicationWorkflowRequestDTO;
import org.beaconfire.application.entity.ApplicationWorkFlow;

import java.util.List;
import java.util.Optional;


public interface ApplicationWorkflowService {
    ApplicationWorkFlow createApplicationWorkflow(ApplicationWorkflowRequestDTO requestDTO);
    Optional<ApplicationWorkFlow> findByEmployeeId(String employeeId);
    List<ApplicationWorkFlow> getApplicationsByStatus(String status);
    void updateApplicationStatus(Long applicationId, String status, String comment);
}
