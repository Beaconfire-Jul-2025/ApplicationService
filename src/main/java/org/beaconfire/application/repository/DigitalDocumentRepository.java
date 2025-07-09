package org.beaconfire.application.repository;

import org.beaconfire.application.entity.DigitalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DigitalDocumentRepository extends JpaRepository<DigitalDocument, Long> {
    List<DigitalDocument> findByType(String type);
    List<DigitalDocument> findByIsRequiredTrue();
}
