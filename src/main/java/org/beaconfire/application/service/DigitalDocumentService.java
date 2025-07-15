package org.beaconfire.application.service;

import lombok.RequiredArgsConstructor;
import org.beaconfire.application.dto.DigitalDocumentDto;
import org.beaconfire.application.mapper.DigitalDocumentMapper;
import org.beaconfire.application.model.DigitalDocument;
import org.beaconfire.application.repository.DigitalDocumentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DigitalDocumentService {

    private final DigitalDocumentRepository repository;
    private final DigitalDocumentMapper mapper;

    public Page<DigitalDocumentDto> getAllWithFilters(String type, Boolean isRequired, String title,
                                                      LocalDateTime startDate, LocalDateTime endDate,
                                                      Pageable pageable) {
        Page<DigitalDocument> entities = repository.findByFilters(type, isRequired, title, startDate, endDate, pageable);
        return entities.map(mapper::toDto);
    }

    public Optional<DigitalDocumentDto> getById(Integer id) {
        return repository.findById(id).map(mapper::toDto);
    }

    public DigitalDocumentDto create(DigitalDocumentDto dto) {
        DigitalDocument entity = mapper.toEntity(dto);
        entity.setId(null);
        DigitalDocument saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    public Optional<DigitalDocumentDto> update(Integer id, DigitalDocumentDto dto) {
        return repository.findById(id).map(existing -> {
            existing.setType(dto.getType());
            existing.setIsRequired(dto.getIsRequired());
            existing.setPath(dto.getPath());
            existing.setDescription(dto.getDescription());
            existing.setTitle(dto.getTitle());
            DigitalDocument updated = repository.save(existing);
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

