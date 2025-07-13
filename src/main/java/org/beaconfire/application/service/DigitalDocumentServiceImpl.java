package org.beaconfire.application.service.impl;

import org.beaconfire.application.dto.*;
import org.beaconfire.application.entity.DigitalDocument;
import org.beaconfire.application.repository.DigitalDocumentRepository;
import org.beaconfire.application.service.DigitalDocumentService;
import org.beaconfire.application.exception.DocumentNotFoundException;
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

    @Override
    public DigitalDocumentResponseDTO getDocumentById(Long id) {
        DigitalDocument doc = documentRepository.findById(id)
            .orElseThrow(() -> new DocumentNotFoundException(id));

        return DigitalDocumentResponseDTO.builder()
            .id(doc.getId())
            .type(doc.getType())
            .title(doc.getTitle())
            .description(doc.getDescription())
            .required(doc.isRequired())
            .path(doc.getPath())
            .build();
    }

    @Override
    public void updateDocument(Long documentId, DigitalDocumentUpdateDTO updateDTO) {
        DigitalDocument doc = documentRepository.findById(documentId)
            .orElseThrow(() -> new DocumentNotFoundException(documentId));

        doc.setType(updateDTO.getType());
        doc.setTitle(updateDTO.getTitle());
        doc.setDescription(updateDTO.getDescription());
        doc.setRequired(updateDTO.isRequired());

        documentRepository.save(doc);
    }

    @Override
    public void updateDocumentFilePath(Long documentId, DigitalDocumentFileUpdateDTO dto) {
        DigitalDocument document = documentRepository.findById(documentId)
            .orElseThrow(() -> new DocumentNotFoundException(documentId));

        document.setPath(dto.getPath());
        documentRepository.save(document);
    }
}
