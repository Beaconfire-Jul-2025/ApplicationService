package org.beaconfire.application.service.impl;

import org.beaconfire.application.dto.DigitalDocumentResponseDTO;
import org.beaconfire.application.dto.DigitalDocumentRequestDTO;
import org.beaconfire.application.entity.DigitalDocument;
import org.beaconfire.application.repository.DigitalDocumentRepository;
import org.beaconfire.application.service.DigitalDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DigitalDocumentServiceImpl implements DigitalDocumentService {

    private final DigitalDocumentRepository documentRepository;

    @Autowired
    public DigitalDocumentServiceImpl(DigitalDocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public List<DigitalDocumentResponseDTO> getAllDocuments() {
        List<DigitalDocument> documents = documentRepository.findAll();

        // Map entity to DTO
        return documents.stream()
                .map(doc -> DigitalDocumentResponseDTO.builder()
                        .id(doc.getId())
                        .type(doc.getType())
                        .title(doc.getTitle())
                        .description(doc.getDescription())
                        .required(doc.isRequired())
                        .path(doc.getPath())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Long createDocument(DigitalDocumentRequestDTO requestDTO) {
        DigitalDocument document = DigitalDocument.builder()
            .type(requestDTO.getType())
            .title(requestDTO.getTitle())
            .description(requestDTO.getDescription())
            .required(requestDTO.isRequired())
            .path(requestDTO.getPath())
            .build();

        DigitalDocument saved = documentRepository.save(document);
        return saved.getId();
    }
}
