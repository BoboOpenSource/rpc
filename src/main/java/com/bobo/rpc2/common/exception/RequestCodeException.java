package com.bobo.rpc2.common.exception;

public class RequestCodeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RequestCodeException(String msg) {
		super(msg);
	}

	public RequestCodeException(Throwable throwable) {
		super(throwable);
	}
}
