package org.beaconfire.application.repository;

import org.beaconfire.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByUserId(String userId);
}
