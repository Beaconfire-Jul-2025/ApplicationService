package org.beaconfire.application.controller;

import lombok.RequiredArgsConstructor;
import org.beaconfire.application.dto.DigitalDocumentDto;
import org.beaconfire.application.service.DigitalDocumentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DigitalDocumentController {

    private final DigitalDocumentService service;

    @GetMapping
    public Page<DigitalDocumentDto> getAllDocuments(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Boolean isRequired,
            @RequestParam(required = false) String title,
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

        return service.getAllWithFilters(type, isRequired, title, startDate, endDate, pageable);
    }

    @GetMapping("/{id}")
    public DigitalDocumentDto getDocumentById(@PathVariable Integer id) {
        return service.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
    }

    @PostMapping
    public DigitalDocumentDto createDocument(@Valid @RequestBody DigitalDocumentDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public DigitalDocumentDto updateDocument(@PathVariable Integer id,
                                             @Valid @RequestBody DigitalDocumentDto dto) {
        return service.update(id, dto)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable Integer id) {
        if (!service.delete(id)) {
            throw new IllegalArgumentException("Document not found");
        }
    }
}