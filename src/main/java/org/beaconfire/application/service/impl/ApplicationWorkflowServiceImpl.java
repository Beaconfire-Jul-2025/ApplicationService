package org.beaconfire.application.service.impl;

import org.beaconfire.application.dto.ApplicationWorkflowRequestDTO;
import org.beaconfire.application.entity.ApplicationWorkFlow;
import org.beaconfire.application.repository.ApplicationWorkflowRepository;
import org.beaconfire.application.service.ApplicationWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class ApplicationWorkflowServiceImpl implements ApplicationWorkflowService {

    private final ApplicationWorkflowRepository applicationWorkflowRepository;

    @Autowired
    public ApplicationWorkflowServiceImpl(ApplicationWorkflowRepository applicationWorkflowRepository) {
        this.applicationWorkflowRepository = applicationWorkflowRepository;
    }

    @Override
    public ApplicationWorkFlow saveApplicationWorkflow(ApplicationWorkflowRequestDTO dto) {
        applicationWorkflowRepository.findByEmployeeId(dto.getEmployeeId())
                .ifPresent(existing -> {
                    throw new RuntimeException("Application workflow already exists for this employee and type.");
                });

        ApplicationWorkFlow entity = ApplicationWorkFlow.builder()
                .employeeId(dto.getEmployeeId())
                .applicationType(dto.getApplicationType())
                .status(dto.getStatus())
                .createDate(LocalDateTime.now())
                .lastModificationDate(LocalDateTime.now())
                .build();

        return applicationWorkflowRepository.save(entity);
    }
}
