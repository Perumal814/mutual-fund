package com.uus.mutualfund.common;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class Util {

	@Autowired
	private MessageSource messageSource;

	public String getMessage(String key) {
		if (key == null || key.trim().isEmpty()) {
			return "";
		}
		try {
			return messageSource.getMessage(key, null, Locale.getDefault());
		} catch (Exception e) {
			return "";
		}
	}

	public SuccessResponse apiResponse(String code, Object data) {
		return SuccessResponse.builder().timestamp(LocalDateTime.now()).status("SUCCESS").code(code)
				.message(getMessage(code)).data(data).build();
	}

}
