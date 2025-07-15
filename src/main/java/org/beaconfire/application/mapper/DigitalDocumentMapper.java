package org.beaconfire.application.mapper;

import org.beaconfire.application.dto.DigitalDocumentDto;
import org.beaconfire.application.model.DigitalDocument;
import org.springframework.stereotype.Component;

@Component
public class DigitalDocumentMapper {

    public DigitalDocumentDto toDto(DigitalDocument entity) {
        if (entity == null) {
            return null;
        }

        return DigitalDocumentDto.builder()
                .id(entity.getId())
                .type(entity.getType())
                .isRequired(entity.getIsRequired())
                .path(entity.getPath())
                .description(entity.getDescription())
                .title(entity.getTitle())
                .createDate(entity.getCreateDate())
                .lastModificationDate(entity.getLastModificationDate())
                .build();
    }

    public DigitalDocument toEntity(DigitalDocumentDto dto) {
        if (dto == null) {
            return null;
        }

        return DigitalDocument.builder()
                .id(dto.getId())
                .type(dto.getType())
                .isRequired(dto.getIsRequired())
                .path(dto.getPath())
                .description(dto.getDescription())
                .title(dto.getTitle())
                .createDate(dto.getCreateDate())
                .lastModificationDate(dto.getLastModificationDate())
                .build();
    }
}