package com.bobo.rpc2.common.exception;

public class GetServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public GetServiceException(String msg) {
		super(msg);
	}

	public GetServiceException(Throwable throwable) {
		super(throwable);
	}
}
