package com.example.vacation_api;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {
    
    private final VacationService vacationService;
    
    public ManagerController(VacationService vacationService) {
        this.vacationService = vacationService;
    }
    
    /**
     * Manager views all vacation requests
     */
    @GetMapping("/{managerId}/vacation-requests")
    public ResponseEntity<List<VacationRequest>> getAllVacationRequests(
            @PathVariable Long managerId,
            @RequestParam(required = false) String status) {
        
        List<VacationRequest> requests;
        if (status != null && !status.trim().isEmpty()) {
            if ("pending,approved".equalsIgnoreCase(status) || "approved,pending".equalsIgnoreCase(status)) {
                requests = vacationService.getPendingAndApprovedRequests();
            } else {
                requests = vacationService.getVacationRequestsByStatus(status);
            }
        } else {
            requests = vacationService.getAllVacationRequests();
        }
        
        return ResponseEntity.ok(requests);
    }
    
    /**
     * Manager views vacation requests for a specific employee
     */
    @GetMapping("/{managerId}/employees/{employeeId}/vacation-requests")
    public ResponseEntity<List<VacationRequest>> getEmployeeOverview(
            @PathVariable Long managerId,
            @PathVariable Long employeeId) {
        
        List<VacationRequest> requests = vacationService.getEmployeeOverview(employeeId);
        return ResponseEntity.ok(requests);
    }
    
    /**
     * Manager views overlapping vacation requests for a date range
     */
    @GetMapping("/{managerId}/overlapping-requests")
    public ResponseEntity<List<VacationRequest>> getOverlappingRequests(
            @PathVariable Long managerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<VacationRequest> overlapping = vacationService.getOverlappingRequests(startDate, endDate);
        return ResponseEntity.ok(overlapping);
    }
    
    /**
     * Manager processes (approves or rejects) a vacation request
     */
    @PutMapping("/{managerId}/vacation-requests/{requestId}")
    public ResponseEntity<VacationRequest> processVacationRequest(
            @PathVariable Long managerId,
            @PathVariable Long requestId,
            @RequestParam String action) {
        
        VacationRequest request = vacationService.processVacationRequest(requestId, action, managerId);
        return ResponseEntity.ok(request);
    }
}
