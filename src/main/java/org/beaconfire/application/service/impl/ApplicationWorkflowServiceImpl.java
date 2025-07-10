package org.beaconfire.application.service.impl;

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
    public ApplicationWorkFlow saveApplicationWorkflow(String employeeId, String applicationType, String comment) {
        applicationWorkflowRepository.findByEmployeeId(employeeId)
                .ifPresent(existing -> {
                    throw new RuntimeException("Application workflow already exists for employee ID: " + employeeId);
                });

        ApplicationWorkFlow entity = ApplicationWorkFlow.builder()
                .employeeId(employeeId)
                .applicationType(applicationType)
                .status("PENDING")
                .comment(comment)
                .createDate(LocalDateTime.now())
                .lastModificationDate(LocalDateTime.now())
                .build();

        return applicationWorkflowRepository.save(entity);
    }
}
