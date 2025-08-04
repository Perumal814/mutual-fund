package com.uus.mutualfund;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uus.mutualfund.common.SuccessResponse;
import com.uus.mutualfund.common.Util;
import com.uus.mutualfund.entity.MutualFundEntity;
import com.uus.mutualfund.model.BuyFund;
import com.uus.mutualfund.model.MutualFund;
import com.uus.mutualfund.model.RedeemFund;
import com.uus.mutualfund.service.MutualFundService;

@SpringBootTest
class MutualFundApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	private MutualFundService mutualFundService;

	@MockBean
	private ModelMapper modelMapper;

	@MockBean
	private Util util;

	@MockBean
	private PasswordEncoder passwordEncoder;

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

		SuccessResponse response = util.apiResponse("2002", mutualFundResponse);

		// Mocking behavior
		Mockito.when(modelMapper.map(any(MutualFund.class), eq(MutualFundEntity.class))).thenReturn(fundEntity);
		Mockito.when(mutualFundService.findByFundCodeAndNavDateAndStatus(anyString(), any(), anyBoolean()))
				.thenReturn(Optional.empty());
		Mockito.when(mutualFundService.addFund(any(MutualFundEntity.class))).thenReturn(savedEntity);
		Mockito.when(modelMapper.map(savedEntity, MutualFund.class)).thenReturn(mutualFundResponse);
		Mockito.when(util.apiResponse("2002", mutualFundResponse)).thenReturn(response);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/mutual-funds/funds")
				.contentType(MediaType.APPLICATION_JSON)
				//.header("Authorization", basicAuth("admin", "admin123"))
				.content(new ObjectMapper().writeValueAsString(fundRequest)))
				//.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("2002")).andExpect(jsonPath("$.data.fundCode").value("AXIS123"));
	}

	@Test
	void testBuyFund_Success() throws Exception {
		BuyFund request = new BuyFund();
		request.setFundId(1l);
		request.setUserId(1l);
		request.setUnits(1000.00);

		SuccessResponse response = util.apiResponse("2003", request);

		Mockito.when(util.apiResponse("2003", request)).thenReturn(response);

		mockMvc.perform(post("/api/v1/mutual-funds/buy").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("2003")).andExpect(jsonPath("$.data.fundCode").value("ABC123"));
	}

	@Test
	void testRedeemFund_Success() throws Exception {
		RedeemFund request = new RedeemFund();
		request.setFundId(1l);
		request.setUserId(1l);
		request.setUnits(5.0);

		SuccessResponse response = util.apiResponse("2004", request);

		Mockito.when(util.apiResponse("2004", request)).thenReturn(response);

		mockMvc.perform(post("/api/v1/mutual-funds/redeem").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("2004")).andExpect(jsonPath("$.data.fundCode").value("ABC123"));
	}

	private String basicAuth(String username, String password) {
		String auth = username + ":" + passwordEncoder.encode(password);
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
		return "Basic " + new String(encodedAuth);
	}

}
