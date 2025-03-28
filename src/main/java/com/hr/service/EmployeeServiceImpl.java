package com.hr.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.entity.Employee;
import com.hr.exception.EmployeeNotFoundException;
import com.hr.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeRepository empRepo;
	
	@Override
	public Employee save(Employee employee) {
		
		return empRepo.save(employee);
	}

	@Override
	public List<Employee> findAll() {
		List<Employee> empList = empRepo.findAll();
		return empList;
	}

	@Override
	public Employee findById(Integer id) {
	    return empRepo.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + id));
	}


	@Override
	public void deleteById(Integer id) {
        empRepo.deleteById(id);
	}


	@Override
	public List<Employee> findUpcomingBirthdays() {
		// Retrieve all upcoming birthdays
        List<Employee> upcomingBirthdays = empRepo.findUpcomingBirthdays();

        // Limit the results to 15 (if there are fewer than 10, it'll return all)
        if (upcomingBirthdays.size() > 10) {
            return upcomingBirthdays.subList(0, 10);
        }
		return upcomingBirthdays;
	}

	@Override
	public Optional<Employee> findByEmail(String email) {
		
		return empRepo.findByEmail(email);
	}
}
