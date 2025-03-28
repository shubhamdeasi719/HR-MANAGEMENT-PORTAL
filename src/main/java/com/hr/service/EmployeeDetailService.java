package com.hr.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hr.entity.Employee;
import com.hr.repository.EmployeeRepository;

@Service
public class EmployeeDetailService implements UserDetailsService {
	
	@Autowired
	EmployeeRepository empRepo;
	

	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Original username: " + username);

        if (username.length() < 4 || !username.startsWith("6233")) { // Check prefix and length
            System.out.println("Invalid username format: " + username);
            throw new UsernameNotFoundException("Invalid username format: " + username);
        }

        try {
            String employeeIdStr = username.substring(4);
            int employeeId = Integer.parseInt(employeeIdStr);

            System.out.println("Extracted employee ID: " + employeeId);

            Optional<Employee> employee = empRepo.findById(employeeId);

            if (employee.isPresent()) {
                var empObj = employee.get();
                System.out.println("Employee found: " + empObj.getEmployeeName());
                return User.builder()
                        .username(username) // Keep the original username
                        .password(empObj.getPassword())
                        .roles(empObj.getRole())
                        .build();
            } else {
                System.out.println("Employee not found in database: " + employeeId);
                throw new UsernameNotFoundException("Employee not found: " + username);
            }
        } catch (NumberFormatException e) {
            System.out.println("Number format exception for extracted ID: " + username.substring(4));
            throw new UsernameNotFoundException("Invalid employee ID format: " + username);
        }
    }
	

}
