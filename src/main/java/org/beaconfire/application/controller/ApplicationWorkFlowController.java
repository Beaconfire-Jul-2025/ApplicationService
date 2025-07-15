package org.beaconfire.application.controller;

import lombok.RequiredArgsConstructor;
import org.beaconfire.application.dto.ApplicationWorkFlowDto;
import org.beaconfire.application.dto.PageListResponse;
import org.beaconfire.application.model.ApplicationWorkFlow;
import org.beaconfire.application.service.ApplicationWorkFlowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/workflows")
@RequiredArgsConstructor
public class ApplicationWorkFlowController {

    private final ApplicationWorkFlowService service;

    @GetMapping
    public PageListResponse<ApplicationWorkFlowDto> getAllWorkFlows(
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) ApplicationWorkFlow.WorkFlowStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ApplicationWorkFlowDto> workflows = service.getAllWithFilters(employeeId, status, startDate, endDate, pageable);

        return PageListResponse.<ApplicationWorkFlowDto>builder()
                .list(workflows.getContent())
                .current(workflows.getNumber() + 1)
                .pageSize(workflows.getSize())
                .total(workflows.getTotalElements())
                .build();
    }

    @GetMapping("/{id}")
    public ApplicationWorkFlowDto getWorkFlowById(@PathVariable Integer id) {
        return service.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found"));
    }

    @PostMapping
    public ApplicationWorkFlowDto createWorkFlow(@Valid @RequestBody ApplicationWorkFlowDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ApplicationWorkFlowDto updateWorkFlow(@PathVariable Integer id,
                                                 @Valid @RequestBody ApplicationWorkFlowDto dto) {
        return service.update(id, dto)
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteWorkFlow(@PathVariable Integer id) {
        if (!service.delete(id)) {
            throw new IllegalArgumentException("Workflow not found");
        }
    }
}