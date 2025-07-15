package org.beaconfire.application.mapper;

import org.beaconfire.application.dto.ApplicationWorkFlowDto;
import org.beaconfire.application.model.ApplicationWorkFlow;
import org.springframework.stereotype.Component;

@Component
public class ApplicationWorkFlowMapper {

    public ApplicationWorkFlowDto toDto(ApplicationWorkFlow entity) {
        if (entity == null) {
            return null;
        }

        return ApplicationWorkFlowDto.builder()
                .id(entity.getId())
                .employeeId(entity.getEmployeeId())
                .createDate(entity.getCreateDate())
                .lastModificationDate(entity.getLastModificationDate())
                .status(entity.getStatus())
                .comment(entity.getComment())
                .build();
    }

    public ApplicationWorkFlow toEntity(ApplicationWorkFlowDto dto) {
        if (dto == null) {
            return null;
        }

        return ApplicationWorkFlow.builder()
                .id(dto.getId())
                .employeeId(dto.getEmployeeId())
                .createDate(dto.getCreateDate())
                .lastModificationDate(dto.getLastModificationDate())
                .status(dto.getStatus())
                .comment(dto.getComment())
                .build();
    }
}