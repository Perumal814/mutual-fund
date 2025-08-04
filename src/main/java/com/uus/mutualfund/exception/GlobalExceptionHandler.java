package com.uus.mutualfund.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.uus.mutualfund.common.ErrorResponse;
import com.uus.mutualfund.common.SuccessResponse;
import com.uus.mutualfund.common.Util;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	Util util;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
		//Map<String, String> errors = new HashMap<>();
		//ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
		
		List<ErrorResponse> errorMessages = ex.getBindingResult()
			    .getFieldErrors()
			    .stream()
			    .map(err -> new ErrorResponse("E4001", err.getDefaultMessage()))
			    .toList();
		SuccessResponse successResponse = SuccessResponse.builder().timestamp(LocalDateTime.now()).status("ERROR")
				.code("E400").message("VALIDATION_REQUEST").error(errorMessages).build();
		
		return ResponseEntity.badRequest().body(successResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGeneralException(Exception ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return ResponseEntity.status(500).body(error);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<SuccessResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
		ErrorResponse error = ErrorResponse.builder().errorCode(ex.getErrorCode())
				.errorMessage(util.getMessage(ex.getErrorCode())).build();
		SuccessResponse successResponse = SuccessResponse.builder().timestamp(LocalDateTime.now()).status("ERROR")
				.code("E" + ex.getErrorCode()).message("BAD_REQUEST").error(error).build();
		return ResponseEntity.badRequest().body(successResponse);
	}

	/*
	 * @ExceptionHandler(Business.class) public ResponseEntity<?>
	 * handleGeneralException(Exception ex) { Map<String, String> error = new
	 * HashMap<>(); error.put("error", ex.getMessage()); return
	 * ResponseEntity.status(500).body(error); }
	 * 
	 * @ExceptionHandler(Exception.class) public ResponseEntity<?>
	 * handleGeneralException(Exception ex) { Map<String, String> error = new
	 * HashMap<>(); error.put("error", ex.getMessage()); return
	 * ResponseEntity.status(500).body(error); }
	 */

}
