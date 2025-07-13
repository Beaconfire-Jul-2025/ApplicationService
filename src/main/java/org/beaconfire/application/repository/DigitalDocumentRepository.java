package org.beaconfire.application.repository;

import org.beaconfire.application.entity.DigitalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DigitalDocumentRepository extends JpaRepository<DigitalDocument, Long> {
}
