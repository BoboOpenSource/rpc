package com.bobo.rpc2.transport.handler.biz.namesrv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bobo.rpc2.namesrv.ServiceURIContainer;
import com.bobo.rpc2.server.ServiceURI;
import com.bobo.rpc2.transport.RemotingCommand;
import com.bobo.rpc2.transport.ResponseCode;
import com.bobo.rpc2.transport.handler.biz.RequestHandler;
import com.bobo.rpc2.transport.seria.SerializerSupport;

import io.netty.channel.ChannelHandlerContext;

public class RegisterHandler implements RequestHandler {

	private static final Logger log = LoggerFactory.getLogger(RegisterHandler.class);

	@Override
	public void handle(ChannelHandlerContext ctx, RemotingCommand request) {
		byte[] body = request.getBody();
		ServiceURI serviceURI = SerializerSupport.deSerialize(body, ServiceURI.class);
		ServiceURIContainer.addService(serviceURI.getServiceName(), serviceURI);
		log.info("namesrv register a service,info:{}", serviceURI.toString());
		ctx.channel().writeAndFlush(RemotingCommand.newResponseCommand(ResponseCode.OK, request.getCommandId()));
	}
}
