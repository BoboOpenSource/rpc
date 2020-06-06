package com.bobo.rpc2.transport.handler.biz;

import java.util.HashMap;
import java.util.Map;

import com.bobo.rpc2.common.exception.RequestCodeException;
import com.bobo.rpc2.transport.RemotingCommand;
import com.bobo.rpc2.transport.RequestCode;
import com.bobo.rpc2.transport.handler.biz.namesrv.LookupHandler;
import com.bobo.rpc2.transport.handler.biz.namesrv.RegisterHandler;

import io.netty.channel.ChannelHandlerContext;

public class RequestHandlerSupport {

	private static Map<Integer, RequestHandler> handlerMap = new HashMap<>();

	static {
		registerRequestHandler(RequestCode.REGISTER_SERVICE, new RegisterHandler());
		registerRequestHandler(RequestCode.LOOKUP_SERVICE, new LookupHandler());
		registerRequestHandler(RequestCode.HEARTBEAT, new HeartbeatHandler());
		registerRequestHandler(RequestCode.REMOTE_INVOKE, new RemoteInvokeHandler());
	}

	public static RequestHandler getRequestHandler(int code) {
		return handlerMap.get(code);
	}

	public static void registerRequestHandler(int code, RequestHandler handler) {
		handlerMap.put(code, handler);
	}

	public static void handle(ChannelHandlerContext ctx, RemotingCommand request) {
		RequestHandler handler = getRequestHandler(request.getCode());
		if (handler == null) {
			throw new RequestCodeException("unknow request code");
		}
		handler.handle(ctx, request);
	}

}
