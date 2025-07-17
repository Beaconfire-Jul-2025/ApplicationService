package org.beaconfire.application.service;

import org.beaconfire.application.dto.DigitalDocumentDto;
import org.beaconfire.application.mapper.DigitalDocumentMapper;
import org.beaconfire.application.model.DigitalDocument;
import org.beaconfire.application.repository.DigitalDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DigitalDocumentServiceTest {

    @Mock
    private DigitalDocumentRepository repository;

    @Mock
    private DigitalDocumentMapper mapper;

    @InjectMocks
    private DigitalDocumentService service;

    private DigitalDocument entity;
    private DigitalDocumentDto dto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Initialize test data
        entity = new DigitalDocument();
        entity.setId(1);
        entity.setType("PDF");
        entity.setIsRequired(true);
        entity.setPath("/documents/test.pdf");
        entity.setDescription("Test document");
        entity.setTitle("Test Title");

        dto = new DigitalDocumentDto();
        dto.setId(1);
        dto.setType("PDF");
        dto.setIsRequired(true);
        dto.setPath("/documents/test.pdf");
        dto.setDescription("Test document");
        dto.setTitle("Test Title");

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testGetAllWithFilters_Success() {
        // Given
        String type = "PDF";
        Boolean isRequired = true;
        String title = "Test";
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        List<DigitalDocument> entities = Arrays.asList(entity);
        Page<DigitalDocument> entityPage = new PageImpl<>(entities, pageable, 1);

        when(repository.findByFilters(type, isRequired, title, startDate, endDate, pageable))
                .thenReturn(entityPage);
        when(mapper.toDto(entity)).thenReturn(dto);

        // When
        Page<DigitalDocumentDto> result = service.getAllWithFilters(
                type, isRequired, title, startDate, endDate, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().get(0));
        verify(repository).findByFilters(type, isRequired, title, startDate, endDate, pageable);
        verify(mapper, times(1)).toDto(entity);
    }

    @Test
    void testGetAllWithFilters_NullFilters() {
        // Given
        Page<DigitalDocument> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(repository.findByFilters(null, null, null, null, null, pageable))
                .thenReturn(emptyPage);

        // When
        Page<DigitalDocumentDto> result = service.getAllWithFilters(
                null, null, null, null, null, pageable);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(repository).findByFilters(null, null, null, null, null, pageable);
        verify(mapper, never()).toDto(any());
    }

    @Test
    void testGetAllWithFilters_MultipleResults() {
        // Given
        DigitalDocument entity2 = new DigitalDocument();
        entity2.setId(2);
        entity2.setType("DOCX");
        entity2.setIsRequired(false);
        entity2.setTitle("Another Document");

        DigitalDocumentDto dto2 = new DigitalDocumentDto();
        dto2.setId(2);
        dto2.setType("DOCX");
        dto2.setIsRequired(false);
        dto2.setTitle("Another Document");

        List<DigitalDocument> entities = Arrays.asList(entity, entity2);
        Page<DigitalDocument> entityPage = new PageImpl<>(entities, pageable, 2);

        when(repository.findByFilters(any(), any(), any(), any(), any(), eq(pageable)))
                .thenReturn(entityPage);
        when(mapper.toDto(entity)).thenReturn(dto);
        when(mapper.toDto(entity2)).thenReturn(dto2);

        // When
        Page<DigitalDocumentDto> result = service.getAllWithFilters(
                null, null, null, null, null, pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        verify(mapper, times(2)).toDto(any());
    }

    @Test
    void testGetById_Found() {
        // Given
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        // When
        Optional<DigitalDocumentDto> result = service.getById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        assertEquals("PDF", result.get().getType());
        assertEquals(true, result.get().getIsRequired());
        verify(repository).findById(id);
        verify(mapper).toDto(entity);
    }

    @Test
    void testGetById_NotFound() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<DigitalDocumentDto> result = service.getById(id);

        // Then
        assertFalse(result.isPresent());
        verify(repository).findById(id);
        verify(mapper, never()).toDto(any());
    }

    @Test
    void testCreate_Success() {
        // Given
        DigitalDocumentDto inputDto = new DigitalDocumentDto();
        inputDto.setId(100); // Should be ignored
        inputDto.setType("XLSX");
        inputDto.setIsRequired(false);
        inputDto.setPath("/documents/spreadsheet.xlsx");
        inputDto.setDescription("Excel document");
        inputDto.setTitle("Spreadsheet Title");

        DigitalDocument newEntity = new DigitalDocument();
        newEntity.setType("XLSX");
        newEntity.setIsRequired(false);
        newEntity.setPath("/documents/spreadsheet.xlsx");
        newEntity.setDescription("Excel document");
        newEntity.setTitle("Spreadsheet Title");

        DigitalDocument savedEntity = new DigitalDocument();
        savedEntity.setId(2);
        savedEntity.setType("XLSX");
        savedEntity.setIsRequired(false);
        savedEntity.setPath("/documents/spreadsheet.xlsx");
        savedEntity.setDescription("Excel document");
        savedEntity.setTitle("Spreadsheet Title");

        when(mapper.toEntity(inputDto)).thenReturn(newEntity);
        when(repository.save(any(DigitalDocument.class))).thenReturn(savedEntity);
        when(mapper.toDto(savedEntity)).thenReturn(dto);

        // When
        DigitalDocumentDto result = service.create(inputDto);

        // Then
        assertNotNull(result);
        assertEquals(dto, result);
        verify(mapper).toEntity(inputDto);
        verify(repository).save(argThat(entity -> entity.getId() == null));
        verify(mapper).toDto(savedEntity);
    }

    @Test
    void testCreate_WithNullOptionalFields() {
        // Given
        DigitalDocumentDto inputDto = new DigitalDocumentDto();
        inputDto.setType("PDF");
        inputDto.setIsRequired(true);
        inputDto.setPath("/documents/minimal.pdf");
        inputDto.setDescription(null);
        inputDto.setTitle("Minimal Document");

        DigitalDocument newEntity = new DigitalDocument();
        newEntity.setType("PDF");
        newEntity.setIsRequired(true);
        newEntity.setPath("/documents/minimal.pdf");
        newEntity.setDescription(null);
        newEntity.setTitle("Minimal Document");

        DigitalDocument savedEntity = new DigitalDocument();
        savedEntity.setId(3);
        savedEntity.setType("PDF");
        savedEntity.setIsRequired(true);
        savedEntity.setPath("/documents/minimal.pdf");
        savedEntity.setDescription(null);
        savedEntity.setTitle("Minimal Document");

        when(mapper.toEntity(inputDto)).thenReturn(newEntity);
        when(repository.save(any(DigitalDocument.class))).thenReturn(savedEntity);
        when(mapper.toDto(savedEntity)).thenReturn(dto);

        // When
        DigitalDocumentDto result = service.create(inputDto);

        // Then
        assertNotNull(result);
        verify(repository).save(argThat(entity ->
                entity.getId() == null &&
                        entity.getDescription() == null
        ));
    }

    @Test
    void testUpdate_Success() {
        // Given
        Integer id = 1;
        DigitalDocumentDto updateDto = new DigitalDocumentDto();
        updateDto.setType("DOCX");
        updateDto.setIsRequired(false);
        updateDto.setPath("/documents/updated.docx");
        updateDto.setDescription("Updated description");
        updateDto.setTitle("Updated Title");

        DigitalDocument existingEntity = new DigitalDocument();
        existingEntity.setId(id);
        existingEntity.setType("PDF");
        existingEntity.setIsRequired(true);
        existingEntity.setPath("/documents/old.pdf");
        existingEntity.setDescription("Old description");
        existingEntity.setTitle("Old Title");

        DigitalDocument updatedEntity = new DigitalDocument();
        updatedEntity.setId(id);
        updatedEntity.setType("DOCX");
        updatedEntity.setIsRequired(false);
        updatedEntity.setPath("/documents/updated.docx");
        updatedEntity.setDescription("Updated description");
        updatedEntity.setTitle("Updated Title");

        when(repository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(repository.save(any(DigitalDocument.class))).thenReturn(updatedEntity);
        when(mapper.toDto(updatedEntity)).thenReturn(dto);

        // When
        Optional<DigitalDocumentDto> result = service.update(id, updateDto);

        // Then
        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(repository).findById(id);
        verify(repository).save(argThat(entity ->
                entity.getType().equals("DOCX") &&
                        entity.getIsRequired().equals(false) &&
                        entity.getPath().equals("/documents/updated.docx") &&
                        entity.getDescription().equals("Updated description") &&
                        entity.getTitle().equals("Updated Title")
        ));
        verify(mapper).toDto(updatedEntity);
    }

    @Test
    void testUpdate_NotFound() {
        // Given
        Integer id = 999;
        DigitalDocumentDto updateDto = new DigitalDocumentDto();
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<DigitalDocumentDto> result = service.update(id, updateDto);

        // Then
        assertFalse(result.isPresent());
        verify(repository).findById(id);
        verify(repository, never()).save(any());
        verify(mapper, never()).toDto(any());
    }

    @Test
    void testUpdate_PartialUpdate() {
        // Given
        Integer id = 1;
        DigitalDocumentDto updateDto = new DigitalDocumentDto();
        updateDto.setType("PDF"); // Same as existing
        updateDto.setIsRequired(true); // Same as existing
        updateDto.setPath("/documents/new-path.pdf"); // Changed
        updateDto.setDescription(null); // Set to null
        updateDto.setTitle("Updated Title"); // Changed

        DigitalDocument existingEntity = new DigitalDocument();
        existingEntity.setId(id);
        existingEntity.setType("PDF");
        existingEntity.setIsRequired(true);
        existingEntity.setPath("/documents/old.pdf");
        existingEntity.setDescription("Old description");
        existingEntity.setTitle("Old Title");

        when(repository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(repository.save(any(DigitalDocument.class))).thenReturn(existingEntity);
        when(mapper.toDto(any())).thenReturn(dto);

        // When
        Optional<DigitalDocumentDto> result = service.update(id, updateDto);

        // Then
        assertTrue(result.isPresent());
        verify(repository).save(argThat(entity ->
                entity.getPath().equals("/documents/new-path.pdf") &&
                        entity.getDescription() == null &&
                        entity.getTitle().equals("Updated Title")
        ));
    }

    @Test
    void testDelete_Success() {
        // Given
        Integer id = 1;
        when(repository.existsById(id)).thenReturn(true);

        // When
        boolean result = service.delete(id);

        // Then
        assertTrue(result);
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void testDelete_NotFound() {
        // Given
        Integer id = 999;
        when(repository.existsById(id)).thenReturn(false);

        // When
        boolean result = service.delete(id);

        // Then
        assertFalse(result);
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void testGetAllWithFilters_WithSpecificFilters() {
        // Given
        String type = "PDF";
        Boolean isRequired = true;
        String title = null;
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        DigitalDocument filteredEntity = new DigitalDocument();
        filteredEntity.setId(5);
        filteredEntity.setType("PDF");
        filteredEntity.setIsRequired(true);
        filteredEntity.setTitle("Filtered Document");
        List<DigitalDocument> entities = Arrays.asList(filteredEntity);
        Page<DigitalDocument> entityPage = new PageImpl<>(entities, pageable, 1);

        when(repository.findByFilters(type, isRequired, title, startDate, endDate, pageable))
                .thenReturn(entityPage);
        when(mapper.toDto(filteredEntity)).thenReturn(dto);

        // When
        Page<DigitalDocumentDto> result = service.getAllWithFilters(
                type, isRequired, title, startDate, endDate, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository).findByFilters(
                eq("PDF"),
                eq(true),
                isNull(),
                eq(startDate),
                eq(endDate),
                eq(pageable)
        );
    }
}