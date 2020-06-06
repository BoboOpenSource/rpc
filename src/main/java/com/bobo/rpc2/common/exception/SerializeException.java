package com.bobo.rpc2.common.exception;

public class SerializeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SerializeException(String msg) {
		super(msg);
	}

	public SerializeException(Throwable throwable) {
		super(throwable);
	}
}
