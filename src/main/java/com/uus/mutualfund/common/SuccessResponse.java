package com.uus.mutualfund.common;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SuccessResponse {

	private LocalDateTime timestamp;
	private String status;
	private String code;
	private String message;
	private Object data;
	private Object error;

}
