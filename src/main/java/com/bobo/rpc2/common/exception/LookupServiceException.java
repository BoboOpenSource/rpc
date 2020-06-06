package com.bobo.rpc2.common.exception;

public class LookupServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public LookupServiceException(String msg) {
		super(msg);
	}

	public LookupServiceException(Throwable throwable) {
		super(throwable);
	}
}
