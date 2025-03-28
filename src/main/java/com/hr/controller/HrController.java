package com.hr.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hr.entity.Compose;
import com.hr.entity.CreatePost;
import com.hr.entity.Employee;
import com.hr.service.ComposeService;
import com.hr.service.CreatePostService;
import com.hr.service.EmployeeService;

import jakarta.validation.Valid;

@Controller
public class HrController {

	@Autowired
	EmployeeService empService;

	@Autowired
	CreatePostService cpService;
	
	@Autowired
	ComposeService cmpService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	private Employee getLoggedInEmployee() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null || authentication.getName().equals("anonymousUser")) {
	        System.out.println("User is not authenticated");
	        return null;
	    }

	    String username = authentication.getName();
	    System.out.println("in getLoggedInEmployee username: " + username);

	    if (username != null && username.startsWith("6233")) { // Check for the prefix
	        try {
	            String employeeIdStr = username.substring(4); // Extract the ID (4 is the prefix length)
	            int employeeId = Integer.parseInt(employeeIdStr);
	            System.out.println("employee id: " + employeeId);
	            Employee employee = empService.findById(employeeId);
	            if (employee == null) {
	                System.out.println("Employee not found in database");
	            }
	            return employee;
	        } catch (NumberFormatException e) {
	            return null;
	        }
	    } else {
	        return null;
	    }
	}
	 
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error, Model model) {
		if (error != null) {
	        model.addAttribute("error", "Invalid Employee Id or password");
	    }
		
		return "login.html";
	}
	
	@GetMapping("/home")
	public String home(RedirectAttributes redirectAttributes, Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String employeeId = username.substring(4);
        Employee employee = empService.findById(Integer.parseInt(employeeId));
		
        System.out.println("name: "+employee.getEmployeeName());
        System.out.println("employee role: " +employee.getRole());
        System.out.println("password: "+employee.getPassword());
		
		if (employee != null) {
			if(employee.getRole().equals("user"))
			{
				List<Compose> cmpList = cmpService.findByEmployee(employee);
				model.addAttribute("cmpList", cmpList);
				return "redirect:/user/user-dash-board";
			}
			else if(employee.getRole().equals("admin"))
			{
				model.addAttribute("loggedInEmployee", employee);
				
				List<Compose> cmpList = cmpService.findAll();
				model.addAttribute("cmpList", cmpList);
				return "redirect:/admin/dash-board";
			}
			else
			{
				redirectAttributes.addFlashAttribute("error", "Something went wrong , please try again!");
				return "redirect:/login";
			}	
			
		} else {
			redirectAttributes.addFlashAttribute("error", "Invalid Employee Id or Password");
			return "redirect:/login";
		}

	}

	@GetMapping("/admin/dash-board")
	public String dashBoard(Model model) {	        
		Employee loggedInEmployee = getLoggedInEmployee();
		model.addAttribute("loggedInEmployee", loggedInEmployee);
		
	    List<Compose> cmpList = cmpService.findAll();
	    model.addAttribute("cmpList", cmpList);
	
	    List<Employee> birthDayList = empService.findUpcomingBirthdays();
	    
	   
	    int currentYear = LocalDate.now().getYear();
	    
	    for (Employee employee : birthDayList) {
	        LocalDate dob = employee.getDob();
	        if (dob != null) {
	            // Replace the birth year with the current year for display
	            LocalDate updatedDob = dob.withYear(currentYear);
	            employee.setDob(updatedDob); // Set the updated DOB (for display only)
	        }
	    }

	    // Add the modified birthday list to the model for rendering in the view
	    model.addAttribute("birthDayList", birthDayList);
	    
	    return "dash-board.html";
	}

	@GetMapping("/admin/add-employee")
	public String addEmployee(Model model) {
		
		Employee loggedInEmployee = getLoggedInEmployee();

	    // Pass the logged-in employee to the model
	    if (loggedInEmployee != null) {
	        model.addAttribute("loggedInEmployee", loggedInEmployee); // Add logged-in employee to the model
	    }

	    // Always pass a new empty Employee object for the "Add Employee" form
	    model.addAttribute("employee", new Employee()); // Empty Employee object for the form

	    return "add-employee.html";
	}


	@GetMapping("/admin/all-employee")
	public String allEmployee(Model model) {
		
		Employee loggedInEmployee = getLoggedInEmployee();
		model.addAttribute("loggedInEmployee", loggedInEmployee);
		
		List<Employee> allEmployees = empService.findAll();
		model.addAttribute("allEmployees", allEmployees);
		return "all-employee.html";
	}

	@GetMapping("/admin/create-post")
	public String createPost(Model model) {
		
		Employee loggedInEmployee = getLoggedInEmployee();
		model.addAttribute("loggedInEmployee", loggedInEmployee);
		
		List<CreatePost> cpPosts = cpService.findAll(Sort.by(Sort.Order.desc("postDate")));
		model.addAttribute("post", cpPosts);
		return "create-post";
	}

	@GetMapping("/admin/status")
	public String status(Model model) {
		
		Employee loggedInEmployee = getLoggedInEmployee();
		model.addAttribute("loggedInEmployee", loggedInEmployee);
		
		List<Compose> cmpList = cmpService.findAll();
		
		cmpList.stream().forEach(k->{
			Employee emp = k.getEmployee();
			if (emp != null) {
	            String designation = emp.getDesignation();
	            k.setPosition(designation);
	            String dept = emp.getDepartment();
	            k.setDepartment(dept);
	        }
		});
		
		model.addAttribute("cmpList", cmpList);
		return "status.html";
	}

	@GetMapping("/admin/my-profile")
	public String myProfile(Model model) {
		
		Employee loggedInEmployee = getLoggedInEmployee();
	    // Check if the employee exists
	    if (loggedInEmployee != null) {
	        model.addAttribute("loggedInEmployee", loggedInEmployee);
	        return "my-profile.html";  // Return the profile page
	    } else {
	        model.addAttribute("error", "Employee not found.");
	        return "error";  // Show an error page if the employee does not exist
	    }
	}


	@GetMapping("/admin/setting")
	public String setting(Model model) {
		Employee loggedInEmployee = getLoggedInEmployee();
		model.addAttribute("loggedInEmployee", loggedInEmployee);
		return "setting.html";
	}

	@PostMapping("/admin/save-employee")
	public String saveEmployee(@Valid @ModelAttribute Employee employee, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
	
		if (result.hasErrors()) {
	        // Pass validation errors to the model
			redirectAttributes.addFlashAttribute("employee", employee);
	        
	        // You can add loggedInEmployee again, just in case needed for the template after an error
			model.addAttribute("loggedInEmployee", getLoggedInEmployee());
	        return "add-employee"; // Stay on the form page with error messages
	    }

	    // If status is null, set it to false
	    if (employee.getStatus() == null) {
	        employee.setStatus(false);
	    }

	    // Convert the dob to a String and set it as the password
	    if (employee.getDob() != null) {
	        String dobAsString = employee.getDob().toString();
	        employee.setPassword(passwordEncoder.encode(dobAsString));
	    }
	    
	    try {
	        empService.save(employee);
	        redirectAttributes.addFlashAttribute("success", "Employee record added successfully.");
	        return "redirect:/admin/all-employee";
	    } catch (Exception e) {
	        // Log the exception for further debugging
	        e.printStackTrace(); 
	        redirectAttributes.addFlashAttribute("error", "Error saving employee: " + e.getMessage()); // Inform the user of the error
	        
	        // Re-add loggedInEmployee after error if you want to retain it in the template
	    
	        redirectAttributes.addFlashAttribute("loggedInEmployee", getLoggedInEmployee());

	        return "add-employee"; // Stay on the form page to fix the error
	    }
	}


	@PostMapping("/admin/save-post")
	public String savePost(@ModelAttribute CreatePost createPost) {
		// Define the date format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		// Format the current date and time
		String formattedDate = LocalDateTime.now().format(formatter);

		// Set the formatted date
		createPost.setPostDate(LocalDateTime.parse(formattedDate,formatter));
		cpService.save(createPost);
		return "redirect:/admin/dash-board";
	}
	
	@PostMapping("/admin/update-password")
	public String updatePassword(@RequestParam("password") String password, 
	                             @RequestParam("newPassword1") String newPassword1, 
	                             @RequestParam("newPassword2") String newPassword2, 
	                             RedirectAttributes redirectAttributes) {
	    
		Employee employee = getLoggedInEmployee();
	    if (employee != null && passwordEncoder.matches(password, employee.getPassword()) && newPassword1.equals(newPassword2)) {
	        if (newPassword1.length() >= 8 && newPassword1.matches("^(?=.*[!@#$%^&]).{8,}$")) {
	            employee.setPassword(passwordEncoder.encode(newPassword1));
	            empService.save(employee);
	            redirectAttributes.addFlashAttribute("success","Your password updated successfully. Please login with your new password.");
	            return "redirect:/login";  // Redirect to login after successful update
	        } else {
	        	redirectAttributes.addFlashAttribute("error", "Password must be at least 8 characters long and contain at least one special character from the set: !@#$%^&");
	            return "redirect:/admin/setting";  // Return to settings if password validation fails
	        }
	    } else {
	    	redirectAttributes.addFlashAttribute("error", "Old password does not match or new passwords do not match.");
	        return "redirect:/admin/setting";  // Return to settings if validation fails
	    }
	}
	
	@GetMapping("/admin/update-employee")
	public String updateRecords(@RequestParam("employeeId") int employeeId, Model model) {
		
		Employee loggedInEmployee  = getLoggedInEmployee();

	    // Fetch the employee to be updated by employeeId from the request
	    Employee employee = empService.findById(employeeId);
	    if (employee == null) {
	        model.addAttribute("error", "Employee not found.");
	        return "redirect:/error";  // Redirect to error page if employee is not found
	    }

	    // Pass the employee to be updated to the model (for rendering in the form)
	    model.addAttribute("employee", employee);

	    // Optionally, pass the logged-in employee to the model (if needed for other purposes)
	    model.addAttribute("loggedInEmployee", loggedInEmployee);

	    return "update-employee";  // Render the update employee page with the data
	}


	
	@GetMapping("/user/updateUserProfile")
	public String updateUserRecords(@RequestParam("employeeId") int employeeId, Model model)
	{

		Employee employee = getLoggedInEmployee(); 
		model.addAttribute("employee", employee);
		return "update-user-profile";
	}
	
	
	@PostMapping("/user/edit-user")
	public String editUserRecords(@Valid @ModelAttribute Employee employee, BindingResult result, @RequestParam("image") MultipartFile imageFile, RedirectAttributes redirectAttributes) {
	    		
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("employee",employee);
	        return "update-user-profile";
	    }

	    // Check if an image is uploaded
	    if (imageFile != null && !imageFile.isEmpty()) {
	        try {
	            // Create a unique filename for the image (using UUID or current timestamp)
	            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
	            
	            // Define the directory to store images under the static folder
	            Path path = Paths.get("src/main/resources/static/db/images/" + fileName); // Save under static/images directory
	            
	            // Create the directory if it doesn't exist
	            Files.createDirectories(path.getParent()); // Ensures that the directory is created
	            
	            // Save the image to the file system
	            Files.copy(imageFile.getInputStream(), path);

	            // Store the relative path of the image in the database
	            employee.setImagePath("/db/images/" + fileName); // Relative to the root context
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    } else {
            Employee loggedInEmployee = getLoggedInEmployee();
            if (loggedInEmployee != null && loggedInEmployee.getImagePath() != null) {
                employee.setImagePath(loggedInEmployee.getImagePath());
            }
        }

	    Employee loggedInEmployee = getLoggedInEmployee();

	    if (loggedInEmployee != null) {
	        empService.save(employee); // Save updated employee with the new or retained image path
	        redirectAttributes.addFlashAttribute("success", "Details updated successfully.");
	        return "redirect:/user/user-profile";
	    } else {
	        return "redirect:/error"; // Handle case where employee is not found
	    }
	}

	
	@PostMapping("/admin/edit-employee")
	public String editRecords(@Valid @ModelAttribute Employee employee, BindingResult result, 
	                          @RequestParam("image") MultipartFile imageFile,RedirectAttributes redirectAttributes ,Model model) throws Exception
	{
		
	    if (result.hasErrors()) {
	        System.out.println("Inside has error");
	        result.getAllErrors().forEach(err -> System.out.println(err.getDefaultMessage()));
	        redirectAttributes.addFlashAttribute("employee", employee);
	        
	        model.addAttribute("loggedInEmployee", getLoggedInEmployee());
	       
	        
	        return "update-employee";
	    }

	    // Handle image upload
	    if (imageFile != null && !imageFile.isEmpty()) {
	        try {
	            System.out.println("File uploaded: " + imageFile.getOriginalFilename());
	            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
	            Path path = Paths.get("src/main/resources/static/db/images/" + fileName); // Check this directory
	            Files.createDirectories(path.getParent());
	            Files.copy(imageFile.getInputStream(), path);

	            employee.setImagePath("/db/images/" + fileName); // Save image path relative to the root
	        } catch (IOException e) {
	            e.printStackTrace();
	            model.addAttribute("error", "Error uploading file: " + e.getMessage());
	            return "update-employee";
	        }
	    } else {
	        // Retain the previous image if no new image uploaded
	        Employee existingEmployee = empService.findById(employee.getEmployeeId());
	        if (existingEmployee != null && existingEmployee.getImagePath() != null) {
	            employee.setImagePath(existingEmployee.getImagePath());
	        }
	    }

	    // Proceed with employee details update
	    Integer employeeId2 = employee.getEmployeeId();
	    Employee empById = empService.findById(employeeId2);
	    
	    if (empById == null) {
	        return "redirect:/error";
	    }

	    try {
	        empService.save(employee);
	        redirectAttributes.addFlashAttribute("success", "Employee record updated successfully.");
	        return "redirect:/admin/all-employee";
	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("error", "Error saving employee: " + e.getMessage());
	        model.addAttribute("loggedInEmployee", getLoggedInEmployee());
	        return "update-employee";
	    }
	}

	@GetMapping("/admin/delete-employee")
	public String deleteRecords(@RequestParam("employeeId") int employeeId, RedirectAttributes redirectAttributes) {
	    try {

	        Employee employee = empService.findById(employeeId);
	        List<Compose> composeList = cmpService.findByEmployee(employee);
	        for (Compose compose : composeList) {
	            cmpService.delete(compose);
	        }

	        empService.deleteById(employeeId);
	        redirectAttributes.addFlashAttribute("success", "Employee record deleted successfully.");
	        return "redirect:/admin/all-employee";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}
	
	@GetMapping("/user/user-dash-board")
    public String userDashBoard(Model model) {
		
		Employee employee = getLoggedInEmployee();
        model.addAttribute("employee", employee);

        // Retrieve the list of composed items related to the user
        List<Compose> cmpList = cmpService.findByEmployee(employee);
        model.addAttribute("cmpList", cmpList);

        // Retrieve the list of upcoming birthdays
        List<Employee> birthDayList = empService.findUpcomingBirthdays();

        // Get the current year
        int currentYear = LocalDate.now().getYear();

        // Loop through the birthday list and update the dob to have the current year
        for (Employee emp : birthDayList) {
            // Retrieve the original dob
            LocalDate dob = emp.getDob();

            if (dob != null) {
                // Replace the year of the dob with the current year
                LocalDate updatedDob = dob.withYear(currentYear);

                // Optionally, set it back to the Employee object (this is still a LocalDate)
                emp.setDob(updatedDob);
            }
        }

        // Add the updated birthday list to the model
        model.addAttribute("birthDayList", birthDayList);

        return "user-dash-board";
    }
	
	@GetMapping("/user/user-profile")
	public String userProfile(Model model) {
		
		Employee employee = getLoggedInEmployee();
		if(employee != null)
		{
			model.addAttribute("employee", employee);
			return "user-profile";
		}else {
			model.addAttribute("error", "Employee not found.");
			return "redirect:/error";
		}
		
	}
	
	@GetMapping("/user/user-setting")
	public String userSetting(Model model) {
		
		Employee employee = getLoggedInEmployee();
	     model.addAttribute("employee", employee);
		return "user-setting";
	}
	
	@PostMapping("/user/update-user-password")
	public String updateUserPassword(@RequestParam("password") String password, 
	                             @RequestParam("newPassword1") String newPassword1, 
	                             @RequestParam("newPassword2") String newPassword2, 
	                             RedirectAttributes redirectAttributes) {
	   
		Employee employee = getLoggedInEmployee();
	    if (employee != null && passwordEncoder.matches(password, employee.getPassword()) && newPassword1.equals(newPassword2)) {
	        if (newPassword1.length() >= 8 && newPassword1.matches("^(?=.*[!@#$%^&]).{8,}$")) {
	            employee.setPassword(passwordEncoder.encode(newPassword1));
	            empService.save(employee);
	            redirectAttributes.addFlashAttribute("success","Your password updated successfully. Please login with your new password");
	            return "redirect:/login";  // Redirect to login after successful update
	        } else {
	        	redirectAttributes.addFlashAttribute("error", "Password must be at least 8 characters long and contain at least one special character from the set: !@#$%^&");
	            return "redirect:/user/user-setting";  // Return to settings if password validation fails
	        }
	    } else {
	    	redirectAttributes.addFlashAttribute("error", "Old password does not match or new passwords do not match.");
	        return "redirect:/user/user-setting";  // Return to settings if validation fails
	    }
	}
	
	@GetMapping("/user/user-compose")
	public String userCompose(Model model) {

		Employee employee = getLoggedInEmployee();
	     model.addAttribute("employee", employee);
		return "user-compose";
	}
	
	@PostMapping("/user/compose")
	public String addCompose(@RequestParam("subject") String subject, @RequestParam("text") String text,RedirectAttributes redirectAttributes)
	{
		Employee employee = getLoggedInEmployee();

        if (employee != null) {
            Compose comp = new Compose();
            comp.setEmpName(employee.getEmployeeName());
            comp.setEmployee(employee);
            comp.setSubject(subject);
            comp.setText(text);
            comp.setAddedDate(new Date().toString());
            comp.setStatus("pending");
            cmpService.save(comp);
        } else {
        	redirectAttributes.addFlashAttribute("error", "Employee not found.");
            return "redirect:/error";
        }
        redirectAttributes.addFlashAttribute("success", "Compose successfully send to HR Department");
		    return "redirect:/user/user-dash-board";
	}
	
	@GetMapping("/admin/admin-action")
	public String composeAction(@RequestParam("id") int id, @RequestParam("type") String type, RedirectAttributes redirectAttributes) {
		
		Compose cmp = cmpService.findById(id);
		cmp.setStatus(type);
		cmpService.save(cmp);
		
		redirectAttributes.addFlashAttribute("success","Information sent to the employee successfully");
		return "redirect:/admin/dash-board";
	}
	
	@GetMapping("/error")
	public String error(Model model, RedirectAttributes redirectAttributes) {
	    // Check for flash attributes (error messages from redirects)
	    if (redirectAttributes.getFlashAttributes().containsKey("error")) {
	        model.addAttribute("error", redirectAttributes.getFlashAttributes().get("error"));
	    }
	    
	    return "error";
	}
}
