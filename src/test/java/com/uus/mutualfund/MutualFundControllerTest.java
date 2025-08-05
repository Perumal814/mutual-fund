package com.uus.mutualfund;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
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
import com.uus.mutualfund.controller.MutualFundController;
import com.uus.mutualfund.entity.MutualFundEntity;
import com.uus.mutualfund.model.BuyFund;
import com.uus.mutualfund.model.MutualFund;
import com.uus.mutualfund.model.RedeemFund;
import com.uus.mutualfund.service.MutualFundService;

@WebMvcTest(MutualFundController.class)
@Import(TestSecurityConfig.class)
public class MutualFundControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MutualFundService mutualFundService;

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
	void testAddFund_Success() throws Exception {
		MutualFund fundRequest = new MutualFund();
		fundRequest.setFundCode("AXIS123");
		fundRequest.setFundName("AXIS Mutual Fund");
		fundRequest.setNavValue(145.75);

		MutualFundEntity fundEntity = new MutualFundEntity();
		fundEntity.setFundCode("AXIS123");
		fundEntity.setFundName("AXIS Mutual Fund");
		fundEntity.setNavDate(LocalDate.now());
		fundEntity.setNavValue(145.75);
		fundEntity.setStatus(true);

		MutualFundEntity savedEntity = new MutualFundEntity();
		savedEntity.setFundId(1L);
		savedEntity.setFundCode("AXIS123");
		savedEntity.setFundName("AXIS Mutual Fund");

		MutualFund mutualFundResponse = new MutualFund();
		mutualFundResponse.setFundCode("AXIS123");
		mutualFundResponse.setFundName("AXIS Mutual Fund");

		SuccessResponse response = new SuccessResponse(null, "SUCCESS", "2002", "Fund added", mutualFundResponse, null);

		when(modelMapper.map(any(MutualFund.class), eq(MutualFundEntity.class))).thenReturn(fundEntity);
		when(mutualFundService.findByFundCodeAndNavDateAndStatus(eq("AXIS123"), any(LocalDate.class), eq(true)))
				.thenReturn(Optional.empty());
		when(mutualFundService.addFund(fundEntity)).thenReturn(savedEntity);
		when(modelMapper.map(savedEntity, MutualFund.class)).thenReturn(mutualFundResponse);
		when(util.apiResponse(eq("2002"), any(MutualFund.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/mutual-funds/funds").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", basicAuth())
				.content(new ObjectMapper().writeValueAsString(fundRequest))).andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("2002")).andExpect(jsonPath("$.data.fundCode").value("AXIS123"));
	}

	@Test
	void testBuyFund_Success() throws Exception {
		BuyFund request = new BuyFund();
		request.setFundId(1L);
		request.setUserId(1L);
		request.setUnits(1000.00);

		SuccessResponse response = new SuccessResponse(null, "SUCCESS", "2003", "Buy Success", request, null);

		when(util.apiResponse(eq("2003"), any(BuyFund.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/mutual-funds/buy").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", basicAuth()).content(new ObjectMapper().writeValueAsString(request)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.code").value("2003"));
		// .andExpect(jsonPath("$.data.fundId").value(ll));
	}

	@Test
	void testRedeemFund_Success() throws Exception {
		RedeemFund request = new RedeemFund();
		request.setFundId(1L);
		request.setUserId(1L);
		request.setUnits(5.0);

		SuccessResponse response = new SuccessResponse(null, "SUCCESS", "2004", "Redeem Success", request, null);

		when(util.apiResponse(eq("2004"), any(RedeemFund.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/mutual-funds/redeem").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", basicAuth()).content(new ObjectMapper().writeValueAsString(request)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.code").value("2004"));
		// .andExpect(jsonPath("$.data.fundCode").value("ABC123"));
	}

}
