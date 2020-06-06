package com.bobo.rpc2.transport;

public class RpcRequest{
	private String serviceName;
	private String methodName;
	private byte[] param;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public byte[] getParam() {
		return param;
	}

	public void setParam(byte[] param) {
		this.param = param;
	}

	public RpcRequest(String serviceName, String methodName, byte[] param) {
		this.serviceName = serviceName;
		this.methodName = methodName;
		this.param = param;
	}

}
