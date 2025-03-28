package com.hr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.hr.service.EmployeeDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations 
{
	@Autowired
	EmployeeDetailService empDetailService;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
	{
		return httpSecurity
			.authorizeHttpRequests
			(registry->{
				registry.requestMatchers("/css/**", "/js/**", "/images/**","/db/**").permitAll();
				registry.requestMatchers("/login","/forgot-password","/error","/home","/send-otp","/verify-otp","/reset-password").permitAll();
				registry.requestMatchers("/admin/**").hasRole("admin");
				registry.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("admin");
				registry.requestMatchers("/user/**").hasRole("user");
				registry.anyRequest().authenticated();
			
			})
			.formLogin(formLogin -> formLogin
                    .loginPage("/login") // Use your custom login page URL
                    .loginProcessingUrl("/perform_login")
                    .defaultSuccessUrl("/home", true)
                    .permitAll()
            )
			 .logout(logout -> logout  // *** ADD THIS LOGOUT CONFIGURATION ***
		                .logoutUrl("/logout") // The URL the form submits to (POST)
		                .invalidateHttpSession(true) // Invalidate the session
		                .clearAuthentication(true) // Clear the authentication
		                .logoutSuccessUrl("/login") // Redirect to /login after logout
		                .permitAll() // Allow access to /logout
		            )
			.build(); 
	}
	
	
	@Bean
	UserDetailsService userDetailsService()
	{
		return empDetailService;
	}
	
	@Bean
	AuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(empDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	@Bean
	PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
}

