package com.bobo.rpc2.namesrv;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bobo.rpc2.server.ServiceURI;

public class ServiceURIContainer {

	private static Map<String, ServiceURI> uriMap = new ConcurrentHashMap<>();

	public static void addService(String serviceName, ServiceURI uri) {
		uriMap.put(serviceName, uri);
	}

	public static ServiceURI getService(String serviceName) {
		return uriMap.get(serviceName);
	}
}
