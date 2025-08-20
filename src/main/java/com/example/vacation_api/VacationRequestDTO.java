package com.example.vacation_api;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class VacationRequestDTO {
    private Long authorId; // Will be set by controller from URL path
    
    @NotNull(message = "Vacation start date is required")
    private LocalDate vacationStartDate;
    
    @NotNull(message = "Vacation end date is required")
    private LocalDate vacationEndDate;
    
    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;
    
    // Constructors
    public VacationRequestDTO() {}
    
    public VacationRequestDTO(Long authorId, LocalDate vacationStartDate, LocalDate vacationEndDate, String reason) {
        this.authorId = authorId;
        this.vacationStartDate = vacationStartDate;
        this.vacationEndDate = vacationEndDate;
        this.reason = reason;
    }
    
    // Getters and setters
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    
    public LocalDate getVacationStartDate() { return vacationStartDate; }
    public void setVacationStartDate(LocalDate vacationStartDate) { this.vacationStartDate = vacationStartDate; }
    
    public LocalDate getVacationEndDate() { return vacationEndDate; }
    public void setVacationEndDate(LocalDate vacationEndDate) { this.vacationEndDate = vacationEndDate; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}