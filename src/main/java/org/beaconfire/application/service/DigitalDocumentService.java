package org.beaconfire.application.service;

import org.beaconfire.application.dto.DigitalDocumentResponseDTO;
import org.beaconfire.application.dto.DigitalDocumentRequestDTO;
import org.beaconfire.application.dto.DigitalDocumentUpdateDTO;

import java.util.List;


public interface DigitalDocumentService {
    List<DigitalDocumentResponseDTO> getAllDocuments();
    Long createDocument(DigitalDocumentRequestDTO requestDTO);
    DigitalDocumentResponseDTO getDocumentById(Long id);
    void updateDocument(Long documentId, DigitalDocumentUpdateDTO updateDTO);
}
