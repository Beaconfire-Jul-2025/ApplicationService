package org.beaconfire.application.controller;

import org.beaconfire.application.dto.DigitalDocumentResponseDTO;
import org.beaconfire.application.service.DigitalDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
}
