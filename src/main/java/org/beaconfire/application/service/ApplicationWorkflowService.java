package org.beaconfire.application.service;

import org.beaconfire.application.dto.ApplicationWorkflowRequestDTO;
import org.beaconfire.application.entity.ApplicationWorkFlow;


public interface ApplicationWorkflowService {
    ApplicationWorkFlow saveApplicationWorkflow(ApplicationWorkflowRequestDTO dto);
}
