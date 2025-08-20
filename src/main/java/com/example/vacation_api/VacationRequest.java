package com.example.vacation_api;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "vacation_requests")
public class VacationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @JsonProperty("author")
    private Employee author;
    
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;
    
    @ManyToOne
    @JoinColumn(name = "resolved_by_id")
    @JsonProperty("resolved_by")
    private Employee resolvedBy;
    
    @CreationTimestamp
    @JsonProperty("request_created_at")
    private LocalDateTime requestCreatedAt;
    
    @Column(nullable = false)
    @JsonProperty("vacation_start_date")
    private LocalDate vacationStartDate;
    
    @Column(nullable = false)
    @JsonProperty("vacation_end_date")
    private LocalDate vacationEndDate;
    
    @Column(length = 500)
    private String reason;
    
    // Constructors
    public VacationRequest() {}
    
    public VacationRequest(Employee author, LocalDate vacationStartDate, LocalDate vacationEndDate, String reason) {
        this.author = author;
        this.vacationStartDate = vacationStartDate;
        this.vacationEndDate = vacationEndDate;
        this.reason = reason;
        this.status = RequestStatus.PENDING;
    }
    
    // Helper method to calculate vacation days
    public long getVacationDays() {
        return ChronoUnit.DAYS.between(vacationStartDate, vacationEndDate) + 1;
    }
    
    // Helper method to check if this request overlaps with another
    public boolean overlapsWith(VacationRequest other) {
        return !this.vacationEndDate.isBefore(other.vacationStartDate) && 
               !other.vacationEndDate.isBefore(this.vacationStartDate);
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Employee getAuthor() { return author; }
    public void setAuthor(Employee author) { this.author = author; }
    
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    
    public Employee getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(Employee resolvedBy) { this.resolvedBy = resolvedBy; }
    
    public LocalDateTime getRequestCreatedAt() { return requestCreatedAt; }
    public void setRequestCreatedAt(LocalDateTime requestCreatedAt) { this.requestCreatedAt = requestCreatedAt; }
    
    public LocalDate getVacationStartDate() { return vacationStartDate; }
    public void setVacationStartDate(LocalDate vacationStartDate) { this.vacationStartDate = vacationStartDate; }
    
    public LocalDate getVacationEndDate() { return vacationEndDate; }
    public void setVacationEndDate(LocalDate vacationEndDate) { this.vacationEndDate = vacationEndDate; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}