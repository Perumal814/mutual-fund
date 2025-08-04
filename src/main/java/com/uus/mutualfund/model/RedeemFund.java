package com.uus.mutualfund.model;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
@Schema(description = "Redeem request for mutual fund units")
public class RedeemFund {

	@Schema(description = "User ID", example = "1")
	@NotNull(message = "User ID is required")
	private Long userId;

	@Schema(description = "Fund Id", example = "1")
	@NotNull(message = "Fund ID is required")
	private Long fundId;

	@Schema(description = "Units to redeem", example = "50")
	@DecimalMin(value = "1.0", message = "Units must be at least 1.0")
	private double units;

	@Schema(description = "NAV Date should be current Date", example = "2025-08-04")
	@PastOrPresent(message = "NAV date must be today not a future date")
	@FutureOrPresent(message = "NAV date must be today not a past date")
	private LocalDate navDate;

}
