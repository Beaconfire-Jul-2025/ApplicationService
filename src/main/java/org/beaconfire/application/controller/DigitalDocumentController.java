package org.beaconfire.application.controller;

import lombok.RequiredArgsConstructor;
import org.beaconfire.application.dto.DigitalDocumentDto;
import org.beaconfire.application.service.DigitalDocumentService;
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
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DigitalDocumentController {

    private final DigitalDocumentService service;

    @GetMapping
    public ResponseEntity<Page<DigitalDocumentDto>> getAllDocuments(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Boolean isRequired,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<DigitalDocumentDto> result = service.getAllWithFilters(type, isRequired, title, startDate, endDate, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DigitalDocumentDto> getDocumentById(@PathVariable Integer id) {
        Optional<DigitalDocumentDto> document = service.getById(id);
        return document.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DigitalDocumentDto> createDocument(@Valid @RequestBody DigitalDocumentDto dto) {
        DigitalDocumentDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DigitalDocumentDto> updateDocument(@PathVariable Integer id,
                                                             @Valid @RequestBody DigitalDocumentDto dto) {
        Optional<DigitalDocumentDto> updated = service.update(id, dto);
        return updated.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Integer id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
