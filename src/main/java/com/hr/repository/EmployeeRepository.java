package com.hr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hr.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	
	@Query("SELECT e FROM Employee e WHERE " +
		       "(MONTH(e.dob) > MONTH(CURRENT_DATE) OR " +
		       "(MONTH(e.dob) = MONTH(CURRENT_DATE) AND DAY(e.dob) > DAY(CURRENT_DATE))) ORDER BY e.dob ASC")
	public List<Employee> findUpcomingBirthdays();

	public Optional<Employee> findByEmail(String email);

}
