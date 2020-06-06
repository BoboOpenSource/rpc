package com.bobo.rpc2.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceProviders {

	private static Map<String, Object> providersMap = new ConcurrentHashMap<>();

	public static void registerService(String serviceName, Object o) {
		providersMap.put(serviceName, o);
	}

	public static Object getService(String serviceName) {
		return providersMap.get(serviceName);
	}
}
