package org.beaconfire.application.repository;

import org.beaconfire.application.model.ApplicationWorkFlow;
import org.beaconfire.application.model.ApplicationWorkFlow.WorkFlowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ApplicationWorkFlowRepository extends JpaRepository<ApplicationWorkFlow, Integer> {

    Page<ApplicationWorkFlow> findByEmployeeIdContainingIgnoreCase(String employeeId, Pageable pageable);

    Page<ApplicationWorkFlow> findByStatus(WorkFlowStatus status, Pageable pageable);

    Page<ApplicationWorkFlow> findByCreateDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT aw FROM ApplicationWorkFlow aw WHERE " +
            "(:employeeId IS NULL OR UPPER(aw.employeeId) LIKE UPPER(CONCAT('%', :employeeId, '%'))) AND " +
            "(:status IS NULL OR aw.status = :status) AND " +
            "(:startDate IS NULL OR aw.createDate >= :startDate) AND " +
            "(:endDate IS NULL OR aw.createDate <= :endDate)")
    Page<ApplicationWorkFlow> findByFilters(@Param("employeeId") String employeeId,
                                            @Param("status") WorkFlowStatus status,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate,
                                            Pageable pageable);
}
