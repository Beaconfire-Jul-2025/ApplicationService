package org.beaconfire.application.service;

import org.beaconfire.application.dto.DigitalDocumentResponseDTO;

import java.util.List;


public interface DigitalDocumentService {
    List<DigitalDocumentResponseDTO> getAllDocuments();
}
