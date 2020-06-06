package com.bobo.rpc2.common;

import io.netty.buffer.ByteBuf;

public class ByteBufHelper {

	private static final String EMPTY_STR="";

	public static void writeString(ByteBuf out,String source) {
		if(source==null||source.length()==0) {
			return;
		}
		byte[] arr=source.getBytes(CharsetHelper.UTF8);
		out.writeInt(arr.length);
		out.writeBytes(arr);
	}

	public static String readString(ByteBuf in) {
		int len=in.readInt();
		if(len<=0) {
			return EMPTY_STR;
		}
		byte[] arr=new byte[len];
		in.readBytes(arr);
		return new String(arr,CharsetHelper.UTF8);
	}

}
