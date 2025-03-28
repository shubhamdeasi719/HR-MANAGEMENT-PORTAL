package com.hr.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.hr.entity.Compose;
import com.hr.entity.CreatePost;
import com.hr.entity.Employee;
import com.hr.service.ComposeService;
import com.hr.service.CreatePostService;
import com.hr.service.EmployeeService;


@SpringBootTest
@AutoConfigureMockMvc
public class HrControllerTest {
	
	@Mock
	private EmployeeService employeeService;
	
	@Mock
	private CreatePostService createPostService;
	
	@Mock
	private ComposeService composeService;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@InjectMocks
	private HrController hrController;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setup()
	{
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(hrController).build();
	}
	
	@Test
	public void testLogin_Get() throws Exception
	{
		mockMvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(view().name("login.html"));
	}
	
	@WithMockUser(username="62332" , roles = {"admin"})
	@Test
	public void testHome_Admin() throws Exception
	{
		// Authentication authentication = mock(Authentication.class);
		// SecurityContextHolder.getContext().setAuthentication(authentication);
		// when(authentication.getName()).thenReturn("62332");
		
		Employee employee = new Employee();
		employee.setRole("admin");
		when(employeeService.findById(2)).thenReturn(employee);
		
		mockMvc.perform(get("/home"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/dash-board"));
		
	}
	
	
	@WithMockUser(username="62332")
	@Test
	public void testHome_User() throws Exception
	{
		// Authentication authentication = mock(Authentication.class);
		// SecurityContextHolder.getContext().setAuthentication(authentication);
		// when(authentication.getName()).thenReturn("62332");
		
		Employee employee =  new Employee();
		employee.setRole("user");
		when(employeeService.findById(2)).thenReturn(employee);
		
		mockMvc.perform(get("/home"))
						.andExpect(status().is3xxRedirection())
						.andExpect(redirectedUrl("/user/user-dash-board"));
		
	}
	
	@WithMockUser(username="62332" , roles = {"admin"})
	@Test
	public void testHome_InvalidEmployee() throws Exception
	{
		when(employeeService.findById(2)).thenReturn(null);
		
		mockMvc.perform(get("/home"))
			   .andExpect(status().is3xxRedirection())
			   .andExpect(redirectedUrl("/login"));		
		
	}
	
	@WithMockUser(username="62332" , roles = {"admin"})
	@Test
	public void testDashBoard_Get() throws Exception
	{
		// Authentication authentication = mock(Authentication.class);
		// SecurityContextHolder.getContext().setAuthentication(authentication);
		// when(authentication.getName()).thenReturn("62332");
		
		Employee employee = new Employee();
		employee.setEmployeeId(2);
		
		when(employeeService.findById(2)).thenReturn(employee);
		
		List<Compose> cmpList = new ArrayList<Compose>();
		cmpList.add(new Compose());
		when(composeService.findAll()).thenReturn(cmpList);
		
		
		List<Employee> empBirthDayList = new ArrayList<Employee>();
		empBirthDayList.add(new Employee());
		when(employeeService.findUpcomingBirthdays()).thenReturn(empBirthDayList);
		
		mockMvc.perform(get("/admin/dash-board"))
				.andExpect(status().isOk())
				.andExpect(view().name("dash-board.html"));
		
	}
	
	@WithMockUser(username="62332" , roles = {"admin"})
	@Test
	public void testAddEmployee_Get() throws Exception
	{		
		Employee employee = new Employee();
		employee.setEmployeeId(2);
		
		when(employeeService.findById(2)).thenReturn(employee);
		
		mockMvc.perform(get("/admin/add-employee"))
				.andExpect(status().isOk())
				.andExpect(view().name("add-employee.html"));
	}
	
	@WithMockUser(username = "62332", roles="admin")
	@Test
	public void testAllEmployee_Get() throws Exception
	{
		Employee employee = new Employee();
		employee.setEmployeeId(2);
		
		when(employeeService.findById(2)).thenReturn(employee);
		
		
		List<Employee> empList = new ArrayList<Employee>();
		empList.add(employee);
		when(employeeService.findAll()).thenReturn(empList);
		
		mockMvc.perform(get("/admin/all-employee"))
				.andExpect(status().isOk())
				.andExpect(view().name("all-employee.html"));
		
	}
	
	@WithMockUser(username = "62332",roles="admin")
	@Test
	public void testCreatePost_Get()
	{
		Employee employee = new Employee();
		employee.setEmployeeId(2);
		
		when(employeeService.findById(2)).thenReturn(employee);
		
		CreatePost cp1 = new CreatePost();
		cp1.setPostId(1);
		cp1.setPostTitle("About India");
		cp1.setPostDiscription("India is 3rd largest economy in world");
		
		CreatePost cp2 = new CreatePost();
		cp2.setPostId(2);
		cp2.setPostTitle("About China");
		cp2.setPostDiscription("China is 2nd largest economy in world");
		
		List<CreatePost> postList = new ArrayList<CreatePost>();
		postList.add(cp1);
		postList.add(cp2);
		
		when(createPostService.findAll(any(Sort.class))).thenReturn(postList);		
	}
	
	@WithMockUser(username = "62332",roles="admin")
	@Test
	public void testStatus_Get() throws Exception
	{
		Employee employee =new Employee();
		employee.setEmployeeId(2);
		
		when(employeeService.findById(2)).thenReturn(employee);
		
		List<Compose> cmpList = new ArrayList<Compose>();
		cmpList.add(new Compose());
		when(composeService.findAll()).thenReturn(cmpList);
		
		mockMvc.perform(get("/admin/status"))
			.andExpect(status().isOk())
			.andExpect(view().name("status.html"));
	}
	
	@WithMockUser(username = "62332",roles="admin")
	@Test
	public void testMyProfile_Get() throws Exception {
		 Employee employee = new Employee();
	     employee.setEmployeeId(2);
	     when(employeeService.findById(2)).thenReturn(employee);

	     mockMvc.perform(get("/admin/my-profile"))
	             .andExpect(status().isOk())
	             .andExpect(view().name("my-profile.html"));
	}
	
	@WithMockUser(username = "62332" , roles = "admin")
	@Test
	public void testSetting_Get() throws Exception {
	    
	    Employee employee = new Employee();
	    employee.setEmployeeId(1);
	    when(employeeService.findById(2)).thenReturn(employee);

	    mockMvc.perform(get("/admin/setting"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("setting.html"));
	 }
	
	
	
	
	
}
