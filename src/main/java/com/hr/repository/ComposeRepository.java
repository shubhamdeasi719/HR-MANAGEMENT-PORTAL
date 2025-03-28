package com.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hr.entity.Compose;
import com.hr.entity.Employee;

public interface ComposeRepository extends JpaRepository<Compose, Integer>{
//	public List<Compose> findByParentUserId(Integer parentUserId);
	List<Compose> findByEmployee(Employee employee);
}
