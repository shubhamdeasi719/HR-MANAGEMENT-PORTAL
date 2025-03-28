package com.hr.controller;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hr.entity.Employee;
import com.hr.service.EmployeeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotPasswordController {

	@Autowired
	EmployeeService empService;
	
	@Autowired
	 private JavaMailSender javaMailSender;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@GetMapping("/forgot-password")
	public String forgotPassword() {
		return "forgot-password.html";
	}

	@PostMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email, HttpSession session, Model model) {

		Optional<Employee> optionalEmployee = empService.findByEmail(email);

		if (optionalEmployee.isPresent()) {
			Employee employee = optionalEmployee.get();
			String otp = generateOtp();
            session.setAttribute("otp", otp);
            session.setAttribute("employeeId", employee.getEmployeeId());
            session.setAttribute("email", email);
            session.setAttribute("otpExpiration", System.currentTimeMillis() + 300000);
            sendOtpEmail(email, otp);
            model.addAttribute("success", "OTP sent successfully. Please check your email.");
			return "verifyOtp";
		} else {
			model.addAttribute("error", "Please enter your registered email ID.");
            return "forgot-password";
		}
	}
	
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") String otp, HttpSession session, Model model)
	{
		
		String storedOtp = (String) session.getAttribute("otp");
		Integer employeeId = (Integer) session.getAttribute("employeeId");
		String email  = (String) session.getAttribute("email");
		Long otpExpiration = (Long) session.getAttribute("otpExpiration");
		
		 if (otpExpiration == null || System.currentTimeMillis() > otpExpiration) 
		 {
	            model.addAttribute("error", "OTP expired. Please request a new one.");
	            return "verifyOtp";
	     }
		 
		if (storedOtp != null && storedOtp.equals(otp) && employeeId != null) 
		{
	        session.removeAttribute("otp");
	        session.removeAttribute("email");
	        session.removeAttribute("otpExpiration");
	        return "resetPassword";
	    } 
		else 
		{
	        model.addAttribute("error", "Invalid OTP. Please try again.");
	        return "verifyOtp";
	    }
	}
	
	@PostMapping("/reset-password")
	public String resetEmployeePassword(@RequestParam("password1") String password1,  @RequestParam("password2") String password2,HttpSession session, Model model)
	{
		
		Integer empId = (Integer) session.getAttribute("employeeId");
		
		if(empId!= null && password1.equals(password2))
		{
			Employee employee = empService.findById(empId);
			if(employee != null)
			{
				employee.setPassword(passwordEncoder.encode(password1));
				empService.save(employee);
				session.removeAttribute("employeeId");
				model.addAttribute("success", "Password reset successfully. Please login with your new password.");
                return "login"; 
			}
			else 
			{
                model.addAttribute("error", "Employee not found.");
                return "forgot-password";
            }
		}
		else {
            model.addAttribute("error", "Passwords do not match.");
            return "resetPassword";
        }
	}
	
	private void sendOtpEmail(String email, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP is: " + otp);
        javaMailSender.send(message);
		
	}

	private String generateOtp() {
		 Random random = new Random();
		 int otpValue = 1000 + random.nextInt(9000);
		 return String.valueOf(otpValue);
	}

}
