package com.uus.mutualfund.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "User request for mutual fund")
public class User {

	@NotBlank(message = "Username is required")
	@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
	@Pattern(regexp = "^[A-Za-z0-9_]+$", message = "Username can contain only letters, numbers, and underscores")
	private String username;

	@NotBlank(message = "Password is required")
	@Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$", message = "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character")
	private String password;

	@NotBlank(message = "Role is required")
	@Size(min = 3, max = 50, message = "Role must be between 3 and 50 characters")
	@Pattern(regexp = "^(USER|ADMIN)$", message = "Role must be either USER or ADMIN")
	private String role;

	
}
