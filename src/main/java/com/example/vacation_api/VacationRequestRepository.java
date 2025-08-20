package com.example.vacation_api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {
    List<VacationRequest> findByAuthorId(Long authorId);
    List<VacationRequest> findByAuthorIdAndStatus(Long authorId, RequestStatus status);
    List<VacationRequest> findByStatus(RequestStatus status);
    List<VacationRequest> findByStatusIn(List<RequestStatus> statuses);
    
    // Query to get approved vacation requests for an employee
    List<VacationRequest> findByAuthorIdAndStatusOrderByVacationStartDateAsc(Long authorId, RequestStatus status);
    
    @Query("SELECT vr FROM VacationRequest vr " +
           "WHERE vr.status = 'APPROVED' " +
           "AND ((vr.vacationStartDate <= :endDate AND vr.vacationEndDate >= :startDate))")
    List<VacationRequest> findOverlappingApprovedRequests(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT vr FROM VacationRequest vr " +
           "WHERE vr.status IN ('APPROVED', 'PENDING') " +
           "AND vr.id != :excludeId " +
           "AND ((vr.vacationStartDate <= :endDate AND vr.vacationEndDate >= :startDate))")
    List<VacationRequest> findOverlappingRequests(@Param("startDate") LocalDate startDate, 
                                                 @Param("endDate") LocalDate endDate, 
                                                 @Param("excludeId") Long excludeId);
}