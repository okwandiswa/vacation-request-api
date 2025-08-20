package com.example.vacation_api;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final EmployeeRepository employeeRepository;
    
    public DataInitializer(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Create initial employees and managers
        if (employeeRepository.count() == 0) {
            // Create employees
            Employee john = new Employee("John Smith", "john.smith@company.com", "Engineering", Role.EMPLOYEE);
            Employee jane = new Employee("Jane Doe", "jane.doe@company.com", "Marketing", Role.EMPLOYEE);
            Employee alice = new Employee("Alice Johnson", "alice.johnson@company.com", "Sales", Role.EMPLOYEE);
            
            // Create managers
            Employee manager1 = new Employee("Bob Manager", "bob.manager@company.com", "Management", Role.MANAGER);
            Employee manager2 = new Employee("Sarah Director", "sarah.director@company.com", "Management", Role.MANAGER);
            
            employeeRepository.save(john);
            employeeRepository.save(jane);
            employeeRepository.save(alice);
            employeeRepository.save(manager1);
            employeeRepository.save(manager2);
            
            System.out.println("Created initial employees: John Smith, Jane Doe, Alice Johnson");
            System.out.println("Created initial managers: Bob Manager, Sarah Director");
        }
    }
}
