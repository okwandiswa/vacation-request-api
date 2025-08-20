package com.example.vacation_api;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String department;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.EMPLOYEE;
    
    @Column(nullable = false)
    private Integer totalVacationDays = 30;
    
    @Column(nullable = false)
    private Integer usedVacationDays = 0;
    
    // Constructors
    public Employee() {}
    
    public Employee(String name, String email, String department) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.role = Role.EMPLOYEE;
        this.totalVacationDays = 30;
        this.usedVacationDays = 0;
    }
    
    public Employee(String name, String email, String department, Role role) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.role = role;
        this.totalVacationDays = 30;
        this.usedVacationDays = 0;
    }
    
    // Helper method to get remaining vacation days
    public Integer getRemainingVacationDays() {
        return totalVacationDays - usedVacationDays;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public Integer getTotalVacationDays() { return totalVacationDays; }
    public void setTotalVacationDays(Integer totalVacationDays) { this.totalVacationDays = totalVacationDays; }
    
    public Integer getUsedVacationDays() { return usedVacationDays; }
    public void setUsedVacationDays(Integer usedVacationDays) { this.usedVacationDays = usedVacationDays; }
}