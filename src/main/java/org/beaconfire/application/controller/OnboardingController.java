package org.beaconfire.application.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.beaconfire.application.dto.OnboardingSubmitDTO;
import org.beaconfire.application.entity.ApplicationWorkFlow;
import org.beaconfire.application.service.ApplicationWorkflowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

    private final ApplicationWorkflowService applicationWorkflowService;

    @PostMapping("/submit")
    public ResponseEntity<ApplicationWorkFlow> submitForm(@Valid @RequestBody OnboardingSubmitDTO dto) {
        ApplicationWorkFlow saved = applicationWorkflowService.saveApplicationWorkflow(
                dto.getId(),  // employeeId
                dto.getApplicationType(),
                "Onboarding form submitted."  // comment
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
