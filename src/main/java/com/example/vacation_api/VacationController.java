package com.example.vacation_api;

//import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class VacationController {
    
    private final VacationService vacationService;
    
    public VacationController(VacationService vacationService) {
        this.vacationService = vacationService;
    }
    
    // Employee endpoints
    
    /**
     * Employee creates a new vacation request
     */
    @PostMapping("/{employeeId}/vacation-requests")
    public ResponseEntity<VacationRequest> createVacationRequest(
            @PathVariable Long employeeId,
            @RequestBody VacationRequestDTO requestDTO) {
        System.out.println("POST /employees/" + employeeId + "/vacation-requests called");
        
        // Ensure the DTO has the correct employee ID from the URL path
        requestDTO.setAuthorId(employeeId);
        
        // Manual validation since we're setting authorId after binding
        if (requestDTO.getVacationStartDate() == null) {
            throw new RuntimeException("Vacation start date is required");
        }
        if (requestDTO.getVacationEndDate() == null) {
            throw new RuntimeException("Vacation end date is required");
        }
        
        VacationRequest request = vacationService.createVacationRequest(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }
    
    /**
     * Employee views their own vacation requests
     */
    @GetMapping("/{employeeId}/vacation-requests")
    public ResponseEntity<List<VacationRequest>> getEmployeeVacationRequests(
            @PathVariable Long employeeId,
            @RequestParam(required = false) String status) {
        
        List<VacationRequest> requests;
        if (status != null && !status.trim().isEmpty()) {
            requests = vacationService.getEmployeeVacationRequestsByStatus(employeeId, status);
        } else {
            requests = vacationService.getEmployeeVacationRequests(employeeId);
        }
        
        return ResponseEntity.ok(requests);
    }
    
    /**
     * Employee views their vacation day summary
     */
    @GetMapping("/{employeeId}/vacation-summary")
    public ResponseEntity<VacationSummaryDTO> getEmployeeVacationSummary(@PathVariable Long employeeId) {
        VacationSummaryDTO summary = vacationService.getEmployeeVacationSummary(employeeId);
        return ResponseEntity.ok(summary);
    }
}