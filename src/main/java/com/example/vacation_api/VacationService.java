package com.example.vacation_api;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Arrays;

@Service
@Transactional
public class VacationService {
    
    private final VacationRequestRepository vacationRequestRepository;
    private final EmployeeRepository employeeRepository;
    
    public VacationService(VacationRequestRepository vacationRequestRepository, 
                          EmployeeRepository employeeRepository) {
        this.vacationRequestRepository = vacationRequestRepository;
        this.employeeRepository = employeeRepository;
    }
    
    // Employee methods
    public VacationRequest createVacationRequest(VacationRequestDTO requestDTO) {
        System.out.println("Creating vacation request for employee: " + requestDTO.getAuthorId());
        
        // Validate employee exists
        Employee employee = employeeRepository.findById(requestDTO.getAuthorId())
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + requestDTO.getAuthorId()));
        
        System.out.println("Found employee: " + employee.getName());
        
        // Validate date range
        if (requestDTO.getVacationEndDate().isBefore(requestDTO.getVacationStartDate())) {
            throw new RuntimeException("Vacation end date must be after start date");
        }
        
        // Calculate vacation days requested
        long vacationDaysRequested = ChronoUnit.DAYS.between(
            requestDTO.getVacationStartDate(), 
            requestDTO.getVacationEndDate()
        ) + 1;
        
        // Check if employee has enough remaining vacation days
        Integer currentUsedDays = getCurrentUsedVacationDays(employee.getId());
        long totalAfterRequest = currentUsedDays + vacationDaysRequested;
        
        if (totalAfterRequest > employee.getTotalVacationDays()) {
            throw new RuntimeException(String.format(
                "Not enough vacation days. Requested: %d, Available: %d", 
                vacationDaysRequested, 
                employee.getRemainingVacationDays()
            ));
        }
        
        // Create vacation request
        VacationRequest vacationRequest = new VacationRequest(
            employee, 
            requestDTO.getVacationStartDate(), 
            requestDTO.getVacationEndDate(), 
            requestDTO.getReason()
        );
        
        VacationRequest saved = vacationRequestRepository.save(vacationRequest);
        System.out.println("Saved vacation request with ID: " + saved.getId());
        
        return saved;
    }
    
    public List<VacationRequest> getEmployeeVacationRequests(Long employeeId) {
        return vacationRequestRepository.findByAuthorId(employeeId);
    }
    
    public List<VacationRequest> getEmployeeVacationRequestsByStatus(Long employeeId, String status) {
        RequestStatus requestStatus = RequestStatus.fromString(status);
        return vacationRequestRepository.findByAuthorIdAndStatus(employeeId, requestStatus);
    }
    
    public VacationSummaryDTO getEmployeeVacationSummary(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        // Update used vacation days based on approved requests
        Integer usedDays = getCurrentUsedVacationDays(employeeId);
        employee.setUsedVacationDays(usedDays);
        employeeRepository.save(employee);
        
        return new VacationSummaryDTO(employee);
    }
    
    // Manager methods
    public List<VacationRequest> getAllVacationRequests() {
        return vacationRequestRepository.findAll();
    }
    
    public List<VacationRequest> getVacationRequestsByStatus(String status) {
        RequestStatus requestStatus = RequestStatus.fromString(status);
        return vacationRequestRepository.findByStatus(requestStatus);
    }
    
    public List<VacationRequest> getPendingAndApprovedRequests() {
        List<RequestStatus> statuses = Arrays.asList(RequestStatus.PENDING, RequestStatus.APPROVED);
        return vacationRequestRepository.findByStatusIn(statuses);
    }
    
    public List<VacationRequest> getEmployeeOverview(Long employeeId) {
        return vacationRequestRepository.findByAuthorId(employeeId);
    }
    
    public List<VacationRequest> getOverlappingRequests(LocalDate startDate, LocalDate endDate) {
        return vacationRequestRepository.findOverlappingApprovedRequests(startDate, endDate);
    }
    
    public VacationRequest processVacationRequest(Long requestId, String action, Long managerId) {
        VacationRequest request = vacationRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Vacation request not found with id: " + requestId));
        
        Employee manager = employeeRepository.findById(managerId)
            .orElseThrow(() -> new RuntimeException("Manager not found with id: " + managerId));
        
        // Verify the manager has manager role
        if (manager.getRole() != Role.MANAGER) {
            throw new RuntimeException("Only managers can process vacation requests");
        }
        
        RequestStatus newStatus;
        if ("approve".equalsIgnoreCase(action)) {
            newStatus = RequestStatus.APPROVED;
            
            // Check for overlapping approved requests
            List<VacationRequest> overlapping = vacationRequestRepository.findOverlappingRequests(
                request.getVacationStartDate(), 
                request.getVacationEndDate(), 
                request.getId()
            );
            
            if (!overlapping.isEmpty()) {
                System.out.println("Warning: Approving request that overlaps with existing requests");
            }
            
            // Update employee's used vacation days
            updateEmployeeUsedVacationDays(request.getAuthor().getId());
            
        } else if ("reject".equalsIgnoreCase(action)) {
            newStatus = RequestStatus.REJECTED;
        } else {
            throw new RuntimeException("Invalid action. Use 'approve' or 'reject'");
        }
        
        request.setStatus(newStatus);
        request.setResolvedBy(manager);
        
        VacationRequest saved = vacationRequestRepository.save(request);
        System.out.println("Request " + requestId + " " + action + "d by manager " + manager.getName());
        
        return saved;
    }
    
    // Helper methods - Calculate vacation days in Java instead of SQL
    private Integer getCurrentUsedVacationDays(Long employeeId) {
        List<VacationRequest> approvedRequests = vacationRequestRepository.findByAuthorIdAndStatusOrderByVacationStartDateAsc(
            employeeId, RequestStatus.APPROVED);
        
        return approvedRequests.stream()
                .mapToInt(request -> (int) request.getVacationDays())
                .sum();
    }
    
    private void updateEmployeeUsedVacationDays(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        Integer usedDays = getCurrentUsedVacationDays(employeeId);
        employee.setUsedVacationDays(usedDays);
        employeeRepository.save(employee);
    }
    
    // Validation methods
    public boolean hasEnoughVacationDays(Long employeeId, long requestedDays) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        Integer currentUsedDays = getCurrentUsedVacationDays(employeeId);
        return (currentUsedDays + requestedDays) <= employee.getTotalVacationDays();
    }
    
    public List<VacationRequest> findConflictingRequests(LocalDate startDate, LocalDate endDate, Long excludeRequestId) {
        return vacationRequestRepository.findOverlappingRequests(startDate, endDate, excludeRequestId);
    }
}