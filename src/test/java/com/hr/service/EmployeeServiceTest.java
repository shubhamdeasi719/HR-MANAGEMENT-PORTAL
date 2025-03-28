package com.hr.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hr.entity.Employee;
import com.hr.exception.EmployeeNotFoundException;
import com.hr.repository.EmployeeRepository;

public class EmployeeServiceTest {
    
    @Mock
    EmployeeRepository empRepo;
    
    @InjectMocks
    EmployeeServiceImpl empService; 

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);  // Initializes mocks and injects them
    }

    @Test
    void testSave() {
        // Given: prepare the data
        Employee employee = new Employee();
        employee.setEmployeeId(101);
        employee.setEmployeeName("shubham");
        employee.setEmail("srd123@gmail.com");

        // When: Define the behavior of the mock
        when(empRepo.save(employee)).thenReturn(employee);

        // Call the service method
        Employee saveEmp = empService.save(employee);

        // Verify the interaction with the mock
        verify(empRepo).save(employee);

        // Assert the result
        assertEquals(employee, saveEmp);
    }
    
    @Test
    void testFindAll()
    {
    	List<Employee> empList = new ArrayList<Employee>();
    	
    	Employee employee1 = new Employee();
        employee1.setEmployeeId(101);
        employee1.setEmployeeName("shubham");
        employee1.setEmail("srd123@gmail.com");
        
        Employee employee2 = new Employee();
        employee2.setEmployeeId(102);
        employee2.setEmployeeName("rushikesh");
        employee2.setEmail("rushi123@gmail.com");
        
        empList.add(employee1);
        empList.add(employee2);
        
        when(empRepo.findAll()).thenReturn(empList);
    	
        List<Employee> allEmpList = empService.findAll();
    	
        verify(empRepo).findAll();
        
        assertEquals(empList, allEmpList);
    	
    }
    
    @Test
    void testFindById()
    {
    	Employee employee = new Employee();
    	employee.setEmployeeId(104);
    	
    	int id = employee.getEmployeeId();
    	
    	when(empRepo.findById(id)).thenReturn(Optional.of(employee));  // Mock Optional
    	
    	Employee emp = empService.findById(id);
    	
    	verify(empRepo).findById(id);
    	
    	assertEquals(employee,emp);
    }
    
    @Test
    void testFindById_EmployeeNotFound() {
        // Arrange: Set up an employee ID that doesn't exist
        Integer invalidEmployeeId = 999;  // ID not found in the repository
        
        // Mock the repository to return an empty Optional for the non-existent employee
        when(empRepo.findById(invalidEmployeeId)).thenReturn(Optional.empty());

        // Act and Assert: Expect EmployeeNotFoundException when trying to find the employee
        assertThrows(EmployeeNotFoundException.class, () -> {
            empService.findById(invalidEmployeeId);  // This should throw the exception
        });

        // Verify that the repository's findById was called with the correct ID
        verify(empRepo).findById(invalidEmployeeId);
    }

    @Test
    void testDeleteById()
    {
    	Employee employee = new Employee();
    	employee.setEmployeeId(105);
    	int id = employee.getEmployeeId();
    	
    	when(empRepo.existsById(id)).thenReturn(true);
    	
        // Act: Call the service method to delete the employee by ID
        empService.deleteById(id);
        
        // Assert: Verify that the repository's deleteById method was called with the correct employee ID
        verify(empRepo).deleteById(id);
    	
    }
    
    @Test
    void testFindUpcomingBirthdays() {
        List<Employee> empList = new ArrayList<Employee>();

        // Create Employee 1
        Employee emp1 = new Employee();
        emp1.setEmployeeId(106);
        emp1.setDob(LocalDate.of(2024, 5, 2));  // Correct date setup

        // Create Employee 2
        Employee emp2 = new Employee();
        emp2.setEmployeeId(107);
        emp2.setDob(LocalDate.of(2024, 5, 2));  // Correct date setup

        // Create Employee 3
        Employee emp3 = new Employee();
        emp3.setEmployeeId(108);
        emp3.setDob(LocalDate.of(2024, 5, 2));  // Correct date setup

        // Add employees to the list
        empList.add(emp1);
        empList.add(emp2);
        empList.add(emp3);

        // Mock the repository method
        when(empRepo.findUpcomingBirthdays()).thenReturn(empList);

        // Act: Call the service method to find upcoming birthdays
        List<Employee> upcomingBirthdays = empService.findUpcomingBirthdays();

        // Verify: Check if the repository method was called
        verify(empRepo).findUpcomingBirthdays();

        // Assert: Verify the result
        assertEquals(empList, upcomingBirthdays);
    }
    
    
    @Test
    public void testFindByEmail()
    {

    	String email = "test@example.com";
        Employee employee = new Employee();
        employee.setEmail(email);
        Optional<Employee> expected = Optional.of(employee);
        when(empRepo.findByEmail(email)).thenReturn(expected);

        // Act
        Optional<Employee> actual = empService.findByEmail(email);

        // Assert
        assertEquals(expected, actual);
        assertTrue(actual.isPresent());
        assertEquals(email, actual.get().getEmail());
    	
    }
    
    @Test
    public void testFindByEmail_EmployeeNotFound()
    {
    	String email ="xyz@gmail.com";
    	Optional<Employee> expected = Optional.empty();
    	when(empRepo.findByEmail(email)).thenReturn(expected);
    	
    	Optional<Employee> actual = empService.findByEmail(email);
    	
    	assertEquals(expected, actual);
    	assertTrue(actual.isEmpty());
    	
    }
}
