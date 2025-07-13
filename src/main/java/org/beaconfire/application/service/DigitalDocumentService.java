package org.beaconfire.application.service;

import org.beaconfire.application.dto.*;

import java.util.List;


public interface DigitalDocumentService {
    List<DigitalDocumentResponseDTO> getAllDocuments();
    Long createDocument(DigitalDocumentRequestDTO requestDTO);
    DigitalDocumentResponseDTO getDocumentById(Long id);
    void updateDocument(Long documentId, DigitalDocumentUpdateDTO updateDTO);
    void updateDocumentFilePath(Long documentId, DigitalDocumentFileUpdateDTO dto);
    void deleteDocument(Long documentId);
    String getDocumentPath(Long documentId);
}
