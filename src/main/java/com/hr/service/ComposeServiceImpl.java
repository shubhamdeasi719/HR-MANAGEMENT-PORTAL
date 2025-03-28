package com.hr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.entity.Compose;
import com.hr.entity.Employee;
import com.hr.repository.ComposeRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComposeServiceImpl implements ComposeService {
	
	@Autowired
	ComposeRepository cmpRepo;

	@Override
	public Compose save(Compose compose) {
		return cmpRepo.save(compose);
	}

	@Override
	public List<Compose> findAll() {
		List<Compose> list = cmpRepo.findAll();
		return list;
	}

	@Override
	public Compose findById(Integer id) {
		return cmpRepo.findById(id).get();
	}
	
//	@Override
//	public List<Compose> findByParentUserId(Integer parentUserId)
//	{
//		List<Compose> list = cmpRepo.findByParentUserId(parentUserId);
//		return list;
//	}

	@Override
	public List<Compose> findByEmployee(Employee employee) {
		
		return cmpRepo.findByEmployee(employee);
	}
	
	@Override
	public void delete(Compose compose){
	    cmpRepo.delete(compose);
	}
}
