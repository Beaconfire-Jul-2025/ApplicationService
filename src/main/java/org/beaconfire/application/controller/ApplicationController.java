package org.beaconfire.application.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.beaconfire.application.dto.ApplicationRequestDTO;
import org.beaconfire.application.entity.Application;
import org.beaconfire.application.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/submit")
    public ResponseEntity<Application> submitApplication(@Valid @RequestBody ApplicationRequestDTO requestDTO) {
        Application saved = applicationService.saveApplication(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}

