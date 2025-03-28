package com.hr.service;

import java.util.List;
import java.util.Optional;

import com.hr.entity.Employee;

public interface EmployeeService {
	public Employee save(Employee employee);
	public List<Employee> findAll();
	public Employee findById(Integer id);
	public void deleteById(Integer id);
	public List<Employee> findUpcomingBirthdays();
	public Optional<Employee> findByEmail(String email);
}
