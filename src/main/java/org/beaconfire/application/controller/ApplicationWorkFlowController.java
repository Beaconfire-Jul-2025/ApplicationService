package org.beaconfire.application.controller;

import lombok.RequiredArgsConstructor;
import org.beaconfire.application.dto.ApplicationWorkFlowDto;
import org.beaconfire.application.model.ApplicationWorkFlow;
import org.beaconfire.application.service.ApplicationWorkFlowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/workflows")
@RequiredArgsConstructor
public class ApplicationWorkFlowController {

    private final ApplicationWorkFlowService service;

    @GetMapping
    public ResponseEntity<Page<ApplicationWorkFlowDto>> getAllWorkFlows(
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) ApplicationWorkFlow.WorkFlowStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ApplicationWorkFlowDto> result = service.getAllWithFilters(employeeId, status, startDate, endDate, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationWorkFlowDto> getWorkFlowById(@PathVariable Integer id) {
        Optional<ApplicationWorkFlowDto> workFlow = service.getById(id);
        return workFlow.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApplicationWorkFlowDto> createWorkFlow(@Valid @RequestBody ApplicationWorkFlowDto dto) {
        ApplicationWorkFlowDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationWorkFlowDto> updateWorkFlow(@PathVariable Integer id,
                                                                 @Valid @RequestBody ApplicationWorkFlowDto dto) {
        Optional<ApplicationWorkFlowDto> updated = service.update(id, dto);
        return updated.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkFlow(@PathVariable Integer id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

