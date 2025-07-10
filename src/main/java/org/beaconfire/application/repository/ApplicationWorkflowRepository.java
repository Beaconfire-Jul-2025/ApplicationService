package org.beaconfire.application.repository;

import org.beaconfire.application.entity.ApplicationWorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface ApplicationWorkflowRepository extends JpaRepository<ApplicationWorkFlow, Long> {
    Optional<ApplicationWorkFlow> findByEmployeeId(String employeeId);
}

