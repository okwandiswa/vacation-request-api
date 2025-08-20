package com.example.vacation_api;

public class VacationSummaryDTO {
    private Long employeeId;
    private String employeeName;
    private Integer totalVacationDays;
    private Integer usedVacationDays;
    private Integer remainingVacationDays;
    
    public VacationSummaryDTO() {}
    
    public VacationSummaryDTO(Employee employee) {
        this.employeeId = employee.getId();
        this.employeeName = employee.getName();
        this.totalVacationDays = employee.getTotalVacationDays();
        this.usedVacationDays = employee.getUsedVacationDays();
        this.remainingVacationDays = employee.getRemainingVacationDays();
    }
    
    // Getters and setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    
    public Integer getTotalVacationDays() { return totalVacationDays; }
    public void setTotalVacationDays(Integer totalVacationDays) { this.totalVacationDays = totalVacationDays; }
    
    public Integer getUsedVacationDays() { return usedVacationDays; }
    public void setUsedVacationDays(Integer usedVacationDays) { this.usedVacationDays = usedVacationDays; }
    
    public Integer getRemainingVacationDays() { return remainingVacationDays; }
    public void setRemainingVacationDays(Integer remainingVacationDays) { this.remainingVacationDays = remainingVacationDays; }
}
