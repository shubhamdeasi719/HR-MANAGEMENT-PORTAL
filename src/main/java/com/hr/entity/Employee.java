package com.hr.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor 
@Entity
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer employeeId;

	@NotNull(message = "Employee Name is required")
	@Size(min = 2, max = 100, message = "Employee name should be between 2 and 100 characters.")
	@Pattern(regexp = "^[A-Za-z ]+$", message = "Employee name must only contain letters and spaces.")
	private String employeeName;

	@NotNull(message = "Email required")
	@Email
	private String email;

	@NotNull(message = "Gender is required")
	private String gender;

	@NotNull(message = "Date of birth is required")
	@Past(message = "Date of birth must be a past date.")
	private LocalDate dob;

	@NotNull(message = "Date of joining is required")
	@Past(message = "Date of joining must be a past date.")
	private LocalDate doj;

	@NotNull(message = "Pan number is required")
	@Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Invalid PAN Number format. Expected format: ABCDE1234F")
	private String panNumber;

	@NotNull(message = "Contact number is required")
	@Pattern(regexp = "[789][0-9]{9}", message = "Invalid mobile number. It should start with 7, 8, or 9 and have 10 digits.")
	@Size(min = 10, max = 10, message = "Mobile number must be exactly 10 digits.")
	private String mobileNumber;

	@NotNull(message = "Aadhar number is required")
	@Pattern(regexp = "[0-9]{12}", message = "Invalid Aadhar Number. It should be 12 digits long.")
	private String aadharNumber;

	@NotNull(message = "Bank account number is required")
	@Pattern(regexp = "[0-9]{9,18}", message = "Invalid Bank Account Number. It should be between 9 to 18 digits long.")
	private String bankAccountNumber;

	@NotNull(message = "Departement is required")
	private String department;

	@NotNull(message = "Designation is required")
	private String designation;

	@NotNull(message = "Previous company name is required")
	@Size(min = 5, max = 255, message = "Previous company name should be between 5 and 255 characters.")
	@Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9\\s,.'-]{5,255}$", message = "Previous company name must contain letter and can include letters, numbers, spaces, commas, periods, apostrophes, and hyphens.")
	private String previousCompany;

	@NotNull(message = "PF number is required")
	private String pfNumber;
	
	@NotNull(message = "Salary is required")
	@Positive(message = "Salary must be a positive value.")
	private Double salary;

	@NotNull(message = "Current address is required")
	@Size(min = 10, max = 500, message = "Address should be between 10 and 500 characters.")
	@Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9\\s,.'-]{10,255}$", message = "Current address must contain letter and can include letters, numbers, spaces, commas, periods, apostrophes, and hyphens.")
	private String currentAddress;

	@NotNull(message = "Permanent address is required")
	@Size(min = 10, max = 500, message = "Address should be between 10 and 500 characters.")
	@Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9\\s,.'-]{10,255}$", message = "Permanent address must contain letter and can include letters, numbers, spaces, commas, periods, apostrophes, and hyphens.")
	private String permanentAddress;

	@Column(nullable = false)
	@NotNull(message = "Status must be either active or inactive.")
	private Boolean status; // Will never be null, always true or false
	
	@Column(name = "created_date", updatable = false)
	@CreationTimestamp
	private LocalDateTime createdDate;
	
	@UpdateTimestamp
	private LocalDateTime updatedDate;
	
	@Size(min = 8, message = "Password must be at least 8 characters long.")
	//@Pattern(regexp ="^(?=.*[!@#$%^&]).{8,}$", message="Password must be at least 8 characters long and contain at least one special character from the set: !@#$%^&")
	private String password;
	
	@NotNull(message = "Role is required")
	private String role;
	
	private String imagePath;
}
