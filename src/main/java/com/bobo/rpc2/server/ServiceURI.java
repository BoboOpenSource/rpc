package com.bobo.rpc2.server;

import com.bobo.rpc2.transport.URI;

public class ServiceURI extends URI {
	private String serviceName;

	public ServiceURI(String serviceName, String type, String host, int port) {
		super(type, host, port);
		this.serviceName = serviceName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		return "ServiceURI [" + super.toString() + "serviceName=" + serviceName + "]";
	}
}
