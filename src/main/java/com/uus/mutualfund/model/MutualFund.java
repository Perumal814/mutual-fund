package com.uus.mutualfund.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Redeem request for mutual fund units")
public class MutualFund {

	@NotBlank(message = "Fund Code is required")
	@Size(min = 3, max = 50)
	private String fundCode;

	@NotBlank(message = "Fund Name is required")
	@Size(min = 3, max = 100)
	private String fundName;

	private String fundType;

	@NotNull(message = "NAV is required")
	@DecimalMin(value = "1.0", message = "NAV must be at least 1.0")
	@DecimalMax(value = "10000.0", message = "NAV must be at most 10000.0")
	private double navValue;

}
