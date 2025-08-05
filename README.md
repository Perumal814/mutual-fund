# mutual-fund
Mutual Fund Management
Project overview
		1.	Developed Secure RESTful API for Mutual Fund Management System using Java and Spring Boot, enabling:
		2.	User registration and authentication
		3.	Role-based access for Admin and User
		4.	Buying and redeeming mutual fund units
		5.	Daily NAV management

Setup instructions 
		1.	Clone the Git repository.
		2.	Change the database credentials (MySQL or H2).
		3.	In com.uus.mutualfund.config.SecurityConfig, change:
			.requestMatchers("/api/v1/users-profile/**").hasRole(Role.ADMIN.toString()) to	.requestMatchers("/api/v1/users-profile/**").permitAll()
		4.	Add one user with the ADMIN role using Postman (Postman collections are provided).
		5.	Then, revert the change in SecurityConfig back to:
		    .requestMatchers("/api/v1/users-profile/**").hasRole(Role.ADMIN.toString())
		6.	Now, add one user with the USER role.
		7.	Finally, verify all access controls related to ADMIN and USER roles.

		
API documentation
		1.	Swagger documentation is implemented.
		2.	After starting the application,
		3.	Open http://localhost:8081/swagger-ui/index.html#/ in your browser.

Instructions to run unit tests.
        1. Use header("Authorization", basicAuth("admin", "admin123")) with the username and password you created for each role.
		2. Replace the basicAuth method with role-based credentials and start testing. 
		 
		 


