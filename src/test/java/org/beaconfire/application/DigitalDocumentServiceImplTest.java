package org.beaconfire.application.service.impl;

import org.beaconfire.application.dto.DigitalDocumentResponseDTO;
import org.beaconfire.application.dto.DigitalDocumentRequestDTO;
import org.beaconfire.application.entity.DigitalDocument;
import org.beaconfire.application.repository.DigitalDocumentRepository;
import org.beaconfire.application.exception.DocumentNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class DigitalDocumentServiceImplTest {

    @Mock
    private DigitalDocumentRepository repository;

    @InjectMocks
    private DigitalDocumentServiceImpl service;

    public DigitalDocumentServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    // Success: return list
    @Test
    void getAllDocuments_shouldReturnList_whenDocumentsExist() {
        DigitalDocument doc = DigitalDocument.builder()
                .id(1L)
                .type("Contract")
                .required(true)
                .title("Employee Agreement")
                .description("Company contract")
                .path("s3://bucket/contract.pdf")
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(doc));

        List<DigitalDocumentResponseDTO> result = service.getAllDocuments();

        assertEquals(1, result.size());
        assertEquals("Contract", result.get(0).getType());
        assertTrue(result.get(0).isRequired());
    }

    // Success: return emptyList
    @Test
    void getAllDocuments_shouldReturnEmptyList_whenNoDocuments() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<DigitalDocumentResponseDTO> result = service.getAllDocuments();

        assertTrue(result.isEmpty());
    }

    // Success: return document ID
    @Test
    void createDocument_shouldSaveAndReturnId() {
        DigitalDocumentRequestDTO requestDTO = DigitalDocumentRequestDTO.builder()
                .type("Contract")
                .title("Employee Handbook")
                .description("Welcome doc")
                .required(true)
                .path("s3://bucket/handbook.pdf")
                .build();

        DigitalDocument saved = DigitalDocument.builder()
                .id(100L)
                .type("Contract")
                .title("Employee Handbook")
                .description("Welcome doc")
                .required(true)
                .path("s3://bucket/handbook.pdf")
                .build();

        when(repository.save(any(DigitalDocument.class))).thenReturn(saved);

        Long result = service.createDocument(requestDTO);

        assertEquals(100L, result);
        verify(repository, times(1)).save(any(DigitalDocument.class));
    }

    // Success: return single document
    @Test
    void getDocumentById_shouldReturnDTO_whenFound() {
        DigitalDocument doc = DigitalDocument.builder()
                .id(1L)
                .type("Contract")
                .title("Title A")
                .description("Desc A")
                .required(true)
                .path("s3://doc.pdf")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(doc));

        DigitalDocumentResponseDTO result = service.getDocumentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Contract", result.getType());
        assertTrue(result.isRequired());
    }

    // Failure: throw exception
    @Test
    void getDocumentById_shouldThrow_whenNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(DocumentNotFoundException.class, () -> {
            service.getDocumentById(999L);
        });
    }
}
