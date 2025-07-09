package org.beaconfire.application.service;

import org.beaconfire.application.dto.ApplicationRequestDTO;
import org.beaconfire.application.entity.Application;


public interface ApplicationService {
    Application saveApplication(ApplicationRequestDTO dto);
}
