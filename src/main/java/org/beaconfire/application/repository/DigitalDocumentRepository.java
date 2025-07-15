package org.beaconfire.application.repository;

import org.beaconfire.application.model.DigitalDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DigitalDocumentRepository extends JpaRepository<DigitalDocument, Integer> {

    Page<DigitalDocument> findByTypeContainingIgnoreCase(String type, Pageable pageable);

    Page<DigitalDocument> findByIsRequired(Boolean isRequired, Pageable pageable);

    Page<DigitalDocument> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT dd FROM DigitalDocument dd WHERE " +
            "(:type IS NULL OR UPPER(dd.type) LIKE UPPER(CONCAT('%', :type, '%'))) AND " +
            "(:isRequired IS NULL OR dd.isRequired = :isRequired) AND " +
            "(:title IS NULL OR UPPER(dd.title) LIKE UPPER(CONCAT('%', :title, '%'))) AND " +
            "(:startDate IS NULL OR dd.createDate >= :startDate) AND " +
            "(:endDate IS NULL OR dd.createDate <= :endDate)")
    Page<DigitalDocument> findByFilters(@Param("type") String type,
                                        @Param("isRequired") Boolean isRequired,
                                        @Param("title") String title,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate,
                                        Pageable pageable);
}