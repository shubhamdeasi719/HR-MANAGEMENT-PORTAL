# HR Management Portal (Spring Boot)

This project is a secure and efficient web application for HR management, built using Spring Boot and Thymeleaf. 
It provides functionalities for employee data management, user authentication, and system monitoring.

## Features

* **Employee Management:**
    * Comprehensive CRUD (Create, Read, Update, Delete) operations for employee records.
    * Streamlined employee data handling.
* **User Authentication and Security:**
    * Role-Based Access Control (RBAC) using Spring Security.
    * OTP (One-Time Password) password reset functionality.
    * Secure file uploads for profile pictures.
* **Web Interface:**
    * Dynamic and user-friendly web interface built with Thymeleaf.
* **Data Management:**
    * Efficient data storage and retrieval using MySQL.
    * Data integrity enforced through robust validation techniques.
* **Application Monitoring:**
    * Application health checks and runtime metrics using Spring Actuator.
    * Performance logging and monitoring using Spring AOP.
* **Testing:**
    * Extensive JUnit tests for code reliability and maintainability.

## Technologies Used

* **Backend:**
    * Spring Boot (Spring Security, Spring Data JPA, Spring Actuator, Spring AOP, Spring MVC)
    * Hibernate
    * MySQL
* **Frontend:**
    * HTML, CSS, Bootstrap, JavaScript
* **Testing:**
    * JUnit
    * Mockito
* **Version Control:**
    * Git
    * GitHub
* **Build Tool:**
    * Maven
* **Template Engine**
    * Thymeleaf 

## Setup Instructions

1.  **Prerequisites:**
    * Java Development Kit (JDK) 17 or higher.
    * MySQL installed and running.
    * Maven installed.
    * Git Installed.
2.  **Clone the Repository:**
    ```bash
    git clone [repository URL]
    ```
3.  **Configure MySQL:**
    * Create a database for the application.
    * Update the `application.properties` file with your MySQL database credentials.
4.  **Build the Project:**
    ```bash
    mvn clean install
    ```
5.  **Run the Application:**
    ```bash
    mvn spring-boot:run
    ```
6.  **Access the Application:**
    * Open your web browser and navigate to `http://localhost:8080`.

## Database Setup

* Create a database in your mysql instance.
* Update the `application.properties` with your database username and password.

## Email Service Configuration

To enable email functionality, please configure the following settings in your `application.properties` or `application.yml` file:
   ```properties
   spring.mail.port=[port_number]
   spring.mail.username=[email address]
   spring.mail.password=[your_app_password]
   ```
