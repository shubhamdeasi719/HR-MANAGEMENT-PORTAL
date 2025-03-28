package com.hr.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hr.entity.Compose;
import com.hr.repository.ComposeRepository;

public class ComposeServiceTest {
	
	@Mock
	ComposeRepository cmpRepo;
	
	@InjectMocks
	ComposeServiceImpl cmpService;
	
	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testSave()
	{
		Compose cmp =  new Compose();
		cmp.setId(201);
		cmp.setEmpName("dinesh");
		cmp.setDepartment("development");
		//cmp.setParentUserId(101);
		
		when(cmpRepo.save(cmp)).thenReturn(cmp);
		
		Compose saveCmp = cmpService.save(cmp);
		
		verify(cmpRepo).save(cmp);
		
		assertEquals(cmp, saveCmp);
		
	}
	
	@Test
	void testFindAll()
	{
		List<Compose> cmpList = new ArrayList<Compose>();
		
		Compose cmp1 =  new Compose();
		cmp1.setId(201);
		cmp1.setEmpName("dinesh");
		cmp1.setDepartment("development");
		
		Compose cmp2 =  new Compose();
		cmp2.setId(2021);
		cmp2.setEmpName("virat");
		cmp2.setDepartment("marketing");
		
		Compose cmp3 =  new Compose();
		cmp3.setId(203);
		cmp3.setEmpName("rohit");
		cmp3.setDepartment("security");
		
		cmpList.add(cmp1);
		cmpList.add(cmp2);
		cmpList.add(cmp3);
		
		when(cmpRepo.findAll()).thenReturn(cmpList);
		
		List<Compose> cmpList2 = cmpService.findAll();
		
		verify(cmpRepo).findAll();
		
		assertEquals(cmpList, cmpList2);
		
	}
	
	
	@Test
	void testFindById()
	{
		Compose cmp = new Compose();
		cmp.setId(101);
		int id = cmp.getId();
		when(cmpRepo.findById(id)).thenReturn(Optional.of(cmp));
		
		Compose cmp2 = cmpService.findById(id);
		
		verify(cmpRepo).findById(id);
		
		assertEquals(cmp, cmp2);
		
	}
}
