package org.beaconfire.application.service;

import org.beaconfire.application.dto.DigitalDocumentResponseDTO;
import org.beaconfire.application.dto.DigitalDocumentRequestDTO;

import java.util.List;


public interface DigitalDocumentService {
    List<DigitalDocumentResponseDTO> getAllDocuments();
    Long createDocument(DigitalDocumentRequestDTO requestDTO);
}
