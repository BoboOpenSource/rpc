package com.bobo.rpc2.transport.seria;

import com.alibaba.fastjson.JSON;
import com.bobo.rpc2.common.CharsetHelper;

@SuppressWarnings("unchecked")
public class JsonSerializer implements Serializer {

	@Override
	public byte[] serialize(Object o) {
		if (o instanceof String) {
			return ((String) o).getBytes(CharsetHelper.UTF8);
		}
		return JSON.toJSONString(o).getBytes(CharsetHelper.UTF8);
	}

	@Override
	public <T> T deSerialize(byte[] arr, Class<T> t) {
		if (t == String.class) {
			return (T) new String(arr, CharsetHelper.UTF8);
		}
		return JSON.parseObject(new String(arr, CharsetHelper.UTF8), t);
	}

	@Override
	public String serializeType() {
		return "json";
	}

}
