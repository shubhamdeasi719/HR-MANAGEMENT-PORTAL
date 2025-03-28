package com.hr.service;

import java.util.List;

import com.hr.entity.Compose;
import com.hr.entity.Employee;

public interface ComposeService {
	public Compose save(Compose compose);
	public List<Compose> findAll();
	public Compose findById(Integer id);
//	public List<Compose> findByParentUserId(Integer parentUserId);
	List<Compose> findByEmployee(Employee employee);
	public void delete(Compose compose);
}
