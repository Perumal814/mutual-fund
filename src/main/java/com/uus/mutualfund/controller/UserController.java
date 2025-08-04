package com.uus.mutualfund.controller;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uus.mutualfund.common.SuccessResponse;
import com.uus.mutualfund.common.Util;
import com.uus.mutualfund.entity.UserEntity;
import com.uus.mutualfund.exception.BusinessException;
import com.uus.mutualfund.model.User;
import com.uus.mutualfund.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/users-profile")
@Tag(name = "User Profile Operations", description = "User Profile Operations")
@AllArgsConstructor
public class UserController {

	private final UserService userService;
	private final ModelMapper modelMapper;
	private final Util util;
	private final PasswordEncoder passwordEncoder;
	
	@Operation(summary = "Add the user", description = "Add the user")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Add user successful"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error"),
			@ApiResponse(responseCode = "503", description = "Service Unavailable") })
	@PostMapping("/users")
	public ResponseEntity<SuccessResponse> addUser(@RequestBody @Valid User userDto) {
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		Optional<UserEntity> userInfo = userService.findUserByUsername(userEntity.getUsername());
		if (userInfo.isPresent()) {
			throw new BusinessException("4002");
		}
		userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
		UserEntity savedUser = userService.addUser(userEntity);
		User responseDto = modelMapper.map(savedUser, User.class);
		SuccessResponse apiResponse = util.apiResponse("2001", responseDto);
		return ResponseEntity.ok(apiResponse);
	}

	@Operation(summary = "Delete the user", description = "Delete the user")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted successful"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error"),
			@ApiResponse(responseCode = "503", description = "Service Unavailable") })
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		return userService.deleteUser(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	}

}
