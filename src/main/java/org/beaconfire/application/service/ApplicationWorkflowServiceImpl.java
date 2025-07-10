package org.beaconfire.application.service.impl;

import org.beaconfire.application.dto.ApplicationWorkflowRequestDTO;
import org.beaconfire.application.entity.ApplicationWorkFlow;
import org.beaconfire.application.repository.ApplicationWorkflowRepository;
import org.beaconfire.application.service.ApplicationWorkflowService;
import org.beaconfire.application.exception.ApplicationAlreadyExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Builder;
import java.util.Optional;


@Service
public class ApplicationWorkflowServiceImpl implements ApplicationWorkflowService {

    private final ApplicationWorkflowRepository applicationWorkflowRepository;

    @Autowired
    public ApplicationWorkflowServiceImpl(ApplicationWorkflowRepository applicationWorkflowRepository) {
        this.applicationWorkflowRepository = applicationWorkflowRepository;
    }

    @Override
    public ApplicationWorkFlow createApplicationWorkflow(ApplicationWorkflowRequestDTO requestDTO) {
        // Check if current employee has a on-going application
        // Only one application at a time
        Optional<ApplicationWorkFlow> existing =
            applicationWorkflowRepository.findByEmployeeId(requestDTO.getEmployeeId());

        if (existing.isPresent()) {
            throw new ApplicationAlreadyExistsException(requestDTO.getEmployeeId());
        }

        ApplicationWorkFlow workflow = ApplicationWorkFlow.builder()
            .employeeId(requestDTO.getEmployeeId())
            .applicationType(requestDTO.getApplicationType())
            .status("Pending")
            .build();

        return applicationWorkflowRepository.save(workflow);
    }

    @Override
    public Optional<ApplicationWorkFlow> findByEmployeeId(String employeeId) {
        return applicationWorkflowRepository.findByEmployeeId(employeeId);
    }
}
