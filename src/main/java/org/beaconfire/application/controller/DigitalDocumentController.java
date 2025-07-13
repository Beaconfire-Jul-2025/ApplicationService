package org.beaconfire.application.controller;

import org.beaconfire.application.dto.*;
import org.beaconfire.application.service.DigitalDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/document")
public class DigitalDocumentController {

    private final DigitalDocumentService digitalDocumentService;

    @Autowired
    public DigitalDocumentController(DigitalDocumentService digitalDocumentService) {
        this.digitalDocumentService = digitalDocumentService;
    }

    // Get all document
    @GetMapping
    public ResponseEntity<List<DigitalDocumentResponseDTO>> getAllDocuments() {
        List<DigitalDocumentResponseDTO> documents = digitalDocumentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    // Get single document
    @GetMapping("/{documentId}")
    public ResponseEntity<DigitalDocumentResponseDTO> getDocumentById(@PathVariable Long documentId) {
        DigitalDocumentResponseDTO dto = digitalDocumentService.getDocumentById(documentId);
        return ResponseEntity.ok(dto);
    }

    // Create new document
    @PostMapping
    public ResponseEntity<Map<String, Object>> createDocument(@Valid @RequestBody DigitalDocumentRequestDTO requestDTO) {
        Long docId = digitalDocumentService.createDocument(requestDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("documentId", docId);
        response.put("message", "Document created successfully");

        return ResponseEntity.status(201).body(response);
    }

    // Update document metadata
    @PutMapping("/{documentId}")
    public ResponseEntity<Map<String, Object>> updateDocument(@PathVariable Long documentId,
        @Valid @RequestBody DigitalDocumentUpdateDTO updateDTO) {

        digitalDocumentService.updateDocument(documentId, updateDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Document updated successfully");
        return ResponseEntity.ok(response);
    }

    // Update document file path
    @PutMapping("/{documentId}/file")
    public ResponseEntity<Map<String, Object>> updateDocumentFile(@PathVariable Long documentId,
        @Valid @RequestBody DigitalDocumentFileUpdateDTO dto) {

        digitalDocumentService.updateDocumentFilePath(documentId, dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Document file path updated successfully");
        return ResponseEntity.ok(response);
    }

    // Delete exist document
    @DeleteMapping("/{documentId}")
    public ResponseEntity<Map<String, Object>> deleteDocument(@PathVariable Long documentId) {
        digitalDocumentService.deleteDocument(documentId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Document deleted successfully");
        return ResponseEntity.ok(response);
    }
}
