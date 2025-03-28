package com.hr.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.hr.entity.Employee;
import com.hr.service.EmployeeService;

public class ForgotPasswordControllerTest {
	
	@Mock
	private EmployeeService empService;
	
	@Mock
	private JavaMailSender javaMailSender;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@InjectMocks
	private ForgotPasswordController forgotPasswordcontroller;
	
	private MockMvc mockMvc;
    private MockHttpSession session;
    
    @BeforeEach
    public void setup()
    {
    	MockitoAnnotations.openMocks(this);
    	mockMvc= MockMvcBuilders.standaloneSetup(forgotPasswordcontroller).build();
    	session = new MockHttpSession();
    }
    
    @Test
    public void testForgotPassword_Get() throws Exception
    {
    	mockMvc.perform(get("/forgot-password"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("forgot-password.html"));
    }
    
    @Test
    public void testSendOtp_Success() throws Exception
    {
    	Employee employee = new Employee();
    	employee.setEmployeeId(101);
    	employee.setEmail("abc@gmail.com");
    	
    	when(empService.findByEmail("abc@gmail.com")).thenReturn(Optional.of(employee));
    	
    	mockMvc.perform(post("/send-otp").param("email", "abc@gmail.com").session(session))
    			.andExpect(status().isOk())
    			.andExpect(view().name("verifyOtp"))
    			.andExpect(model().attributeExists("success"));
    	
    	verify(javaMailSender).send(any(SimpleMailMessage.class));
    }
    
    @Test
    public void testSendOtp_EmailNotFound() throws Exception
    {
    	when(empService.findByEmail("xyz@gmail.com")).thenReturn(Optional.empty());
    	
    	mockMvc.perform(post("/send-otp").param("email", "xyz@gmail.com").session(session))
    									.andExpect(status().isOk())
    									.andExpect(view().name("forgot-password"))
    									.andExpect(model().attributeExists("error"));
    	
    }
    
    @Test
    public void testVerifyOtp_Success() throws Exception
    {
    	session.setAttribute("otp", "1234");
    	session.setAttribute("employeeId", 101);
    	session.setAttribute("otpExpiration", System.currentTimeMillis()+300000);
    	
    	mockMvc.perform(post("/verify-otp").param("otp", "1234").session(session))
    										.andExpect(status().isOk())
    										.andExpect(view().name("resetPassword"));
    }


    @Test
    public void testVerifyOtp_InvalidOtp() throws Exception
    {
    	session.setAttribute("otp", "1234");
    	session.setAttribute("employeeId", 101);
    	session.setAttribute("otpExpiration", System.currentTimeMillis()+300000);
    	
    	mockMvc.perform(post("/verify-otp").param("otp", "7894").session(session))
    										.andExpect(status().isOk())
    										.andExpect(view().name("verifyOtp"))
    										.andExpect(model().attributeExists("error"));
    }

    
    @Test
    public void testVerifyOtp_ExpiredOtp() throws Exception
    {
    	session.setAttribute("otp", "1234");
    	session.setAttribute("employeeId", 101);
    	session.setAttribute("otpExpiration", System.currentTimeMillis() - 1000);
    	
    	mockMvc.perform(post("/verify-otp").param("otp", "1234").session(session))
    										.andExpect(status().isOk())
    										.andExpect(view().name("verifyOtp"))
    										.andExpect(model().attributeExists("error"));
    }
    

    @Test
    public void testResetPassword_Success() throws Exception
    {
    	Employee employee = new Employee();
    	employee.setEmployeeId(101);
    	
    	session.setAttribute("employeeId", employee.getEmployeeId());
    	
    	when(empService.findById(employee.getEmployeeId())).thenReturn(employee);
    	when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
    	
    	mockMvc.perform(post("/reset-password")
    			.param("password1", "newPassword")
    			.param("password2", "newPassword")
    			.session(session))
    			.andExpect(status().isOk())
    			.andExpect(view().name("login"))
    			.andExpect(model().attributeExists("success"));
    	
    	verify(empService).save(any(Employee.class));
    
    }
    
    @Test
    public void testResetPassword_passwordMismatch() throws Exception
    {
    	Employee employee = new Employee();
    	employee.setEmployeeId(101);
    	
    	session.setAttribute("employeeId", employee.getEmployeeId());
    	
    	mockMvc.perform(post("/reset-password")
    			.param("password1", "newPassword")
    			.param("password2","differenPassword")
    			.session(session))
    			.andExpect(status().isOk())
    			.andExpect(view().name("resetPassword"))
    			.andExpect(model().attributeExists("error"));   	
    }
    
    
    @Test
    public void testResetPassword_EmployeeNotFound() throws Exception
    {
    	Employee employee = new Employee();
    	employee.setEmployeeId(110);
    	
    	session.setAttribute("employeeId", employee.getEmployeeId());
    	
    	when(empService.findById(employee.getEmployeeId())).thenReturn(null);
    	
    	mockMvc.perform(post("/reset-password")
    			.param("password1", "newPassword")
    			.param("password2", "newPassword")
    			.session(session))
    			.andExpect(status().isOk())
    			.andExpect(view().name("forgot-password"))
    			.andExpect(model().attributeExists("error"));   	
    }


}
