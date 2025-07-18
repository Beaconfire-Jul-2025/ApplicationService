package org.beaconfire.application.service;
import org.beaconfire.application.dto.ApplicationWorkFlowDto;
import org.beaconfire.application.mapper.ApplicationWorkFlowMapper;
import org.beaconfire.application.model.ApplicationWorkFlow;
import org.beaconfire.application.repository.ApplicationWorkFlowRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationWorkFlowServiceTest {

    @Mock
    private ApplicationWorkFlowRepository repository;

    @Mock
    private ApplicationWorkFlowMapper mapper;

    @InjectMocks
    private ApplicationWorkFlowService service;

    private ApplicationWorkFlow entity;
    private ApplicationWorkFlowDto dto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Initialize test data
        entity = new ApplicationWorkFlow();
        entity.setId(1);
        entity.setEmployeeId("EMP001");
        entity.setStatus(ApplicationWorkFlow.WorkFlowStatus.PENDING);
        entity.setComment("Test comment");

        dto = new ApplicationWorkFlowDto();
        dto.setId(1);
        dto.setEmployeeId("EMP001");
        dto.setStatus(ApplicationWorkFlow.WorkFlowStatus.PENDING);
        dto.setComment("Test comment");

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testGetAllWithFilters_Success() {
        // Given
        String employeeId = "EMP001";
        ApplicationWorkFlow.WorkFlowStatus status = ApplicationWorkFlow.WorkFlowStatus.PENDING;
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        List<ApplicationWorkFlow> entities = Arrays.asList(entity);
        Page<ApplicationWorkFlow> entityPage = new PageImpl<>(entities, pageable, 1);

        when(repository.findByFilters(employeeId, status, startDate, endDate, pageable))
                .thenReturn(entityPage);
        when(mapper.toDto(entity)).thenReturn(dto);

        // When
        Page<ApplicationWorkFlowDto> result = service.getAllWithFilters(
                employeeId, status, startDate, endDate, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().get(0));
        verify(repository).findByFilters(employeeId, status, startDate, endDate, pageable);
        verify(mapper, times(1)).toDto(entity);
    }

    @Test
    void testGetAllWithFilters_EmptyResult() {
        // Given
        Page<ApplicationWorkFlow> emptyPage = new PageImpl<>(Arrays.asList(), pageable, 0);
        when(repository.findByFilters(any(), any(), any(), any(), any())).thenReturn(emptyPage);

        // When
        Page<ApplicationWorkFlowDto> result = service.getAllWithFilters(
                null, null, null, null, pageable);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findByFilters(null, null, null, null, pageable);
        verify(mapper, never()).toDto(any());
    }

    @Test
    void testGetById_Found() {
        // Given
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        // When
        Optional<ApplicationWorkFlowDto> result = service.getById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(repository).findById(id);
        verify(mapper).toDto(entity);
    }

    @Test
    void testGetById_NotFound() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<ApplicationWorkFlowDto> result = service.getById(id);

        // Then
        assertFalse(result.isPresent());
        verify(repository).findById(id);
        verify(mapper, never()).toDto(any());
    }

    @Test
    void testCreate_Success() {
        // Given
        ApplicationWorkFlowDto inputDto = new ApplicationWorkFlowDto();
        inputDto.setId(100); // Should be ignored
        inputDto.setEmployeeId("EMP002");
        inputDto.setStatus(ApplicationWorkFlow.WorkFlowStatus.COMPLETED);
        inputDto.setComment("New workflow");

        ApplicationWorkFlow newEntity = new ApplicationWorkFlow();
        newEntity.setEmployeeId("EMP002");
        newEntity.setStatus(ApplicationWorkFlow.WorkFlowStatus.COMPLETED);
        newEntity.setComment("New workflow");

        ApplicationWorkFlow savedEntity = new ApplicationWorkFlow();
        savedEntity.setId(2);
        savedEntity.setEmployeeId("EMP002");
        savedEntity.setStatus(ApplicationWorkFlow.WorkFlowStatus.COMPLETED);
        savedEntity.setComment("New workflow");

        when(mapper.toEntity(inputDto)).thenReturn(newEntity);
        when(repository.save(any(ApplicationWorkFlow.class))).thenReturn(savedEntity);
        when(mapper.toDto(savedEntity)).thenReturn(dto);

        // When
        ApplicationWorkFlowDto result = service.create(inputDto);

        // Then
        assertNotNull(result);
        assertEquals(dto, result);
        verify(mapper).toEntity(inputDto);
        verify(repository).save(argThat(entity -> entity.getId() == null));
        verify(mapper).toDto(savedEntity);
    }

    @Test
    void testUpdate_Success() {
        // Given
        Integer id = 1;
        ApplicationWorkFlowDto updateDto = new ApplicationWorkFlowDto();
        updateDto.setEmployeeId("EMP003");
        updateDto.setStatus(ApplicationWorkFlow.WorkFlowStatus.COMPLETED);
        updateDto.setComment("Updated comment");

        ApplicationWorkFlow existingEntity = new ApplicationWorkFlow();
        existingEntity.setId(id);
        existingEntity.setEmployeeId("EMP001");
        existingEntity.setStatus(ApplicationWorkFlow.WorkFlowStatus.PENDING);
        existingEntity.setComment("Old comment");

        ApplicationWorkFlow updatedEntity = new ApplicationWorkFlow();
        updatedEntity.setId(id);
        updatedEntity.setEmployeeId("EMP003");
        updatedEntity.setStatus(ApplicationWorkFlow.WorkFlowStatus.COMPLETED);
        updatedEntity.setComment("Updated comment");

        when(repository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(repository.save(any(ApplicationWorkFlow.class))).thenReturn(updatedEntity);
        when(mapper.toDto(updatedEntity)).thenReturn(dto);

        // When
        Optional<ApplicationWorkFlowDto> result = service.update(id, updateDto);

        // Then
        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(repository).findById(id);
        verify(repository).save(argThat(entity ->
                entity.getEmployeeId().equals("EMP003") &&
                        entity.getStatus() == ApplicationWorkFlow.WorkFlowStatus.COMPLETED &&
                        entity.getComment().equals("Updated comment")
        ));
        verify(mapper).toDto(updatedEntity);
    }

    @Test
    void testUpdate_NotFound() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<ApplicationWorkFlowDto> result = service.update(id, dto);

        // Then
        assertFalse(result.isPresent());
        verify(repository).findById(id);
        verify(repository, never()).save(any());
        verify(mapper, never()).toDto(any());
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
    void testCreate_NullId() {
        // Given
        ApplicationWorkFlowDto inputDto = new ApplicationWorkFlowDto();
        inputDto.setId(null); // Already null
        inputDto.setEmployeeId("EMP004");

        ApplicationWorkFlow newEntity = new ApplicationWorkFlow();
        newEntity.setEmployeeId("EMP004");

        ApplicationWorkFlow savedEntity = new ApplicationWorkFlow();
        savedEntity.setId(3);
        savedEntity.setEmployeeId("EMP004");

        when(mapper.toEntity(inputDto)).thenReturn(newEntity);
        when(repository.save(any(ApplicationWorkFlow.class))).thenReturn(savedEntity);
        when(mapper.toDto(savedEntity)).thenReturn(dto);

        // When
        ApplicationWorkFlowDto result = service.create(inputDto);

        // Then
        assertNotNull(result);
        verify(repository).save(argThat(entity -> entity.getId() == null));
    }

    @Test
    void testGetAllWithFilters_MultipleResults() {
        // Given
        ApplicationWorkFlow entity2 = new ApplicationWorkFlow();
        entity2.setId(2);
        entity2.setEmployeeId("EMP002");

        ApplicationWorkFlowDto dto2 = new ApplicationWorkFlowDto();
        dto2.setId(2);
        dto2.setEmployeeId("EMP002");

        List<ApplicationWorkFlow> entities = Arrays.asList(entity, entity2);
        Page<ApplicationWorkFlow> entityPage = new PageImpl<>(entities, pageable, 2);

        when(repository.findByFilters(any(), any(), any(), any(), eq(pageable)))
                .thenReturn(entityPage);
        when(mapper.toDto(entity)).thenReturn(dto);
        when(mapper.toDto(entity2)).thenReturn(dto2);

        // When
        Page<ApplicationWorkFlowDto> result = service.getAllWithFilters(
                null, null, null, null, pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        verify(mapper, times(2)).toDto(any());
    }
}