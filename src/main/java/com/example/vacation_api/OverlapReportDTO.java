package com.example.vacation_api;

import java.time.LocalDate;
import java.util.List;

public class OverlapReportDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<VacationRequest> overlappingRequests;
    
    public OverlapReportDTO() {}
    
    public OverlapReportDTO(LocalDate startDate, LocalDate endDate, List<VacationRequest> overlappingRequests) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.overlappingRequests = overlappingRequests;
    }
    
    // Getters and setters
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public List<VacationRequest> getOverlappingRequests() { return overlappingRequests; }
    public void setOverlappingRequests(List<VacationRequest> overlappingRequests) { this.overlappingRequests = overlappingRequests; }
}
