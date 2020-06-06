package com.bobo.rpc2.transport.seria;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import com.bobo.rpc2.common.exception.SerializeException;

public class SerializerSupport {

	private static final Map<String, Serializer> serializerMap = new HashMap<>();

	private static final String DEFAULT_TYPE = System.getProperty("serialize.type", "json");

	static {
		ServiceLoader<Serializer> serviceLoader = ServiceLoader.load(Serializer.class);
		Iterator<Serializer> serializerIte = serviceLoader.iterator();
		while (serializerIte.hasNext()) {
			Serializer serializer = serializerIte.next();
			serializerMap.put(serializer.serializeType(), serializer);
		}
	}

	public static byte[] serialize(String type, Object o) {
		Serializer serializer = serializerMap.get(type);
		if (serializer == null) {
			throw new SerializeException("no such serialize type");
		}
		return serializer.serialize(o);
	}

	public static byte[] serialize(Object o) {
		return serialize(DEFAULT_TYPE, o);
	}

	public static <T> T deSerialize(String type, byte[] arr, Class<T> t) {
		Serializer serializer = serializerMap.get(type);
		if (serializer == null) {
			throw new SerializeException("no such serialize type");
		}
		return serializer.deSerialize(arr, t);
	}

	public static <T> T deSerialize(byte[] arr, Class<T> t) {
		return deSerialize(DEFAULT_TYPE, arr, t);
	}

}
