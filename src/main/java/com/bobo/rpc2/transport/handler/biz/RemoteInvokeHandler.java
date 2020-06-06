package com.bobo.rpc2.transport.handler.biz;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bobo.rpc2.server.ServiceProviders;
import com.bobo.rpc2.transport.RemotingCommand;
import com.bobo.rpc2.transport.ResponseCode;
import com.bobo.rpc2.transport.RpcRequest;
import com.bobo.rpc2.transport.seria.SerializerSupport;

import io.netty.channel.ChannelHandlerContext;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RemoteInvokeHandler implements RequestHandler {

	private static final Logger log = LoggerFactory.getLogger(RemoteInvokeHandler.class);

	@Override
	public void handle(ChannelHandlerContext ctx, RemotingCommand msg) {
		RpcRequest request = SerializerSupport.deSerialize(msg.getBody(), RpcRequest.class);
		String serviceName = request.getServiceName();
		String methodName = request.getMethodName();
		Object obj = ServiceProviders.getService(serviceName);
		if (obj == null) {
			ctx.channel().writeAndFlush(
					RemotingCommand.newResponseCommand(ResponseCode.SERVICE_NOT_FOUND, msg.getCommandId(), null));
			return;
		}
		Method[] methods = obj.getClass().getMethods();
		Method method = null;
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals(methodName)) {
				method = methods[i];
				break;
			}
		}
		if (method == null) {
			ctx.channel().writeAndFlush(
					RemotingCommand.newResponseCommand(ResponseCode.METHOD_NOT_FOUND, msg.getCommandId(), null));
			return;
		}
		Class paramType = method.getParameterTypes()[0];
		Object param = SerializerSupport.deSerialize(request.getParam(), paramType);
		log.info("remote invoke,param:{}", param.toString());
		Object resp;
		try {
			resp = method.invoke(obj, param);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.error("remote invoke error", e);
			ctx.channel().writeAndFlush(
					RemotingCommand.newResponseCommand(ResponseCode.SYSTEM_ERROR, msg.getCommandId(), null));
			return;
		}
		log.info("remote invoke,result:{}", resp);
		ctx.channel().writeAndFlush(RemotingCommand.newResponseCommand(ResponseCode.OK, msg.getCommandId(),
				SerializerSupport.serialize(resp)));
	}

}
