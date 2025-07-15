package org.beaconfire.application.service;

import lombok.RequiredArgsConstructor;
import org.beaconfire.application.dto.ApplicationWorkFlowDto;
import org.beaconfire.application.mapper.ApplicationWorkFlowMapper;
import org.beaconfire.application.model.ApplicationWorkFlow;
import org.beaconfire.application.repository.ApplicationWorkFlowRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationWorkFlowService {

    private final ApplicationWorkFlowRepository repository;
    private final ApplicationWorkFlowMapper mapper;

    public Page<ApplicationWorkFlowDto> getAllWithFilters(String employeeId, ApplicationWorkFlow.WorkFlowStatus status,
                                                          LocalDateTime startDate, LocalDateTime endDate,
                                                          Pageable pageable) {
        Page<ApplicationWorkFlow> entities = repository.findByFilters(employeeId, status, startDate, endDate, pageable);
        return entities.map(mapper::toDto);
    }

    public Optional<ApplicationWorkFlowDto> getById(Integer id) {
        return repository.findById(id).map(mapper::toDto);
    }

    public ApplicationWorkFlowDto create(ApplicationWorkFlowDto dto) {
        ApplicationWorkFlow entity = mapper.toEntity(dto);
        entity.setId(null);
        ApplicationWorkFlow saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    public Optional<ApplicationWorkFlowDto> update(Integer id, ApplicationWorkFlowDto dto) {
        return repository.findById(id).map(existing -> {
            existing.setEmployeeId(dto.getEmployeeId());
            existing.setStatus(dto.getStatus());
            existing.setComment(dto.getComment());
            ApplicationWorkFlow updated = repository.save(existing);
            return mapper.toDto(updated);
        });
    }

    public boolean delete(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}