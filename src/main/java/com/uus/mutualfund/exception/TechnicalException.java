package com.uus.mutualfund.exception;

public class TechnicalException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1207457562236591063L;
	private final String errorCode;

	public TechnicalException(String errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

}
