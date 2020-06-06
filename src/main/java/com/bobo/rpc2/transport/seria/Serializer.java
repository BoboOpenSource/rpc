package com.bobo.rpc2.transport.seria;

public interface Serializer {

	byte[] serialize(Object o);

	<T> T deSerialize(byte[] arr, Class<T> t);

	String serializeType();
}
