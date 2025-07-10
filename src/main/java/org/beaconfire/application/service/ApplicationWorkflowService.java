package org.beaconfire.application.service;

import org.beaconfire.application.entity.ApplicationWorkFlow;


public interface ApplicationWorkflowService {
    ApplicationWorkFlow saveApplicationWorkflow(String employeeId, String applicationType,
            String comment);
}
