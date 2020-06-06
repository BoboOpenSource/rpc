package com.bobo.rpc2.common.exception;

public class InvokeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvokeException(String msg) {
		super(msg);
	}

	public InvokeException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
