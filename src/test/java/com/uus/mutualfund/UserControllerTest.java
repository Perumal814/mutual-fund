package com.uus.mutualfund;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uus.mutualfund.common.SuccessResponse;
import com.uus.mutualfund.common.Util;
import com.uus.mutualfund.controller.UserController;
import com.uus.mutualfund.entity.UserEntity;
import com.uus.mutualfund.model.BuyFund;
import com.uus.mutualfund.model.RedeemFund;
import com.uus.mutualfund.model.User;
import com.uus.mutualfund.service.UserService;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private ModelMapper modelMapper;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@MockBean
	private Util util;

	private String basicAuth() {
		String auth = "Admin:Password@123";
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
		return "Basic " + new String(encodedAuth, StandardCharsets.UTF_8);
	}

	@Test
	void testAddUser_Success() throws Exception {
		User userDto = new User();
		userDto.setUsername("TestUser");
		userDto.setPassword("Password@123");
		userDto.setRole("ADMIN");

		UserEntity userEntity = new UserEntity();
		userEntity.setUsername("TestUser");

		UserEntity savedEntity = new UserEntity();
		savedEntity.setUsername("TestUser");
		SuccessResponse response = new SuccessResponse(null, "SUCCESS", "2001", "User created", userDto, null);

		when(modelMapper.map(any(User.class), eq(UserEntity.class))).thenReturn(userEntity);
		when(userService.findUserByUsername("TestUser")).thenReturn(Optional.empty());
		when(passwordEncoder.encode("Password@123")).thenReturn("hashed-password");
		when(userService.addUser(userEntity)).thenReturn(savedEntity);
		when(modelMapper.map(savedEntity, User.class)).thenReturn(userDto);
		when(util.apiResponse(eq("2001"), any(User.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/users-profile/users").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", basicAuth()).content(new ObjectMapper().writeValueAsString(userDto)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.code").value("2001"))
				.andExpect(jsonPath("$.data.username").value("TestUser"));
	}

	@Test
	void testAddUser_AlreadyExists() throws Exception {
		User userDto = new User();
		userDto.setUsername("TestUser");
		userDto.setPassword("Test@1234");
		userDto.setRole("USER");

		UserEntity existingUser = new UserEntity();
		existingUser.setUsername("TestUser");

		when(modelMapper.map(any(User.class), eq(UserEntity.class))).thenReturn(existingUser);
		when(userService.findUserByUsername("TestUser")).thenReturn(Optional.of(existingUser));

		mockMvc.perform(post("/api/v1/users-profile/users").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", basicAuth()).content(new ObjectMapper().writeValueAsString(userDto)))
				.andExpect(status().isBadRequest());
	}

	
}