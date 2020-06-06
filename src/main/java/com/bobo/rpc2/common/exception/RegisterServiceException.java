package com.bobo.rpc2.common.exception;

public class RegisterServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RegisterServiceException(String msg) {
		super(msg);
	}

	public RegisterServiceException(Throwable throwable) {
		super(throwable);
	}

	public RegisterServiceException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
