package org.beaconfire.application.controller;

import org.beaconfire.application.dto.ApplicationWorkflowRequestDTO;
import org.beaconfire.application.entity.ApplicationWorkFlow;
import org.beaconfire.application.service.ApplicationWorkflowService;
import org.beaconfire.application.exception.ApplicationAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/application")
public class ApplicationWorkflowController {

    private final ApplicationWorkflowService applicationWorkflowService;

    @Autowired
    public ApplicationWorkflowController(ApplicationWorkflowService applicationWorkflowService) {
        this.applicationWorkflowService = applicationWorkflowService;
    }

    @PostMapping
    public ResponseEntity<?> createApplication(@Valid @RequestBody ApplicationWorkflowRequestDTO requestDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        try {
            ApplicationWorkFlow created = applicationWorkflowService.createApplicationWorkflow(requestDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("applicationId", created.getId());
            response.put("message", "Application Created");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ApplicationAlreadyExistsException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("message", ex.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ApplicationWorkFlow>> getPendingApplications() {
        List<ApplicationWorkFlow> result = applicationWorkflowService.getApplicationsByStatus("Pending");
        return ResponseEntity.ok(result);
    }
}
