package com.uus.mutualfund.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uus.mutualfund.common.SuccessResponse;
import com.uus.mutualfund.common.Util;
import com.uus.mutualfund.constants.URIConstants;
import com.uus.mutualfund.entity.MutualFundEntity;
import com.uus.mutualfund.model.BuyFund;
import com.uus.mutualfund.model.MutualFund;
import com.uus.mutualfund.model.RedeemFund;
import com.uus.mutualfund.service.MutualFundService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@Tag(name = "Mutual Fund Operations", description = "Mutual Fund Operations")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(URIConstants.API + "/${application.version1}" + URIConstants.MUTUAL_FUNDS)
public class MutualFundController {

	private final MutualFundService mutualFundService;
	private final ModelMapper modelMapper;
	private final Util util;

	@Operation(summary = "Add Mutual Funds", description = "Add Mutual Funds")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Add Mutual Funds Successful"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error"),
			@ApiResponse(responseCode = "503", description = "Service Unavailable") })
	@PostMapping(URIConstants.FUNDS)
	public ResponseEntity<SuccessResponse> addFund(@RequestBody @Valid MutualFund requestModel) {

		MutualFundEntity fundEntity = modelMapper.map(requestModel, MutualFundEntity.class);
		Optional<MutualFundEntity> fundInfo = mutualFundService
				.findByFundCodeAndNavDateAndStatus(fundEntity.getFundCode(), LocalDate.now(), true);
		if (fundInfo.isPresent()) {
			fundEntity.setFundId(fundInfo.get().getFundId());
		}
		MutualFundEntity savedUser = mutualFundService.addFund(fundEntity);
		MutualFund mutualFundResponse = modelMapper.map(savedUser, MutualFund.class);
		SuccessResponse apiResponse = util.apiResponse("2002", mutualFundResponse);
		return ResponseEntity.ok(apiResponse);

	}

	@Operation(summary = "Buy Mutual Funds", description = "Buy Mutual Funds")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Purchase successful"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error"),
			@ApiResponse(responseCode = "503", description = "Service Unavailable") })
	@PostMapping(URIConstants.BUY)
	public ResponseEntity<SuccessResponse> buy(@RequestBody @Valid BuyFund request) {
		mutualFundService.buyUnits(request);
		SuccessResponse apiResponse = util.apiResponse("2003", request);
		return ResponseEntity.ok(apiResponse);
	}

	@Operation(summary = "Redeem Mutual Funds", description = "Redeem Mutual Funds")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Redemption successful"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error"),
			@ApiResponse(responseCode = "503", description = "Service Unavailable") })
	@PostMapping(URIConstants.REDEEM)
	public ResponseEntity<SuccessResponse> redeem(@RequestBody @Valid RedeemFund request) {
		mutualFundService.redeemUnits(request);
		SuccessResponse apiResponse = util.apiResponse("2004", request);
		return ResponseEntity.ok(apiResponse);
	}

}