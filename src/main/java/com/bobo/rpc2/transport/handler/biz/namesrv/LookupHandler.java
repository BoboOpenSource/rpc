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

public class LookupHandler implements RequestHandler {

	private static final Logger log = LoggerFactory.getLogger(LookupHandler.class);

	@Override
	public void handle(ChannelHandlerContext ctx, RemotingCommand request) {
		byte[] body = request.getBody();
		String serviceName = SerializerSupport.deSerialize(body, String.class);
		ServiceURI serviceUri = ServiceURIContainer.getService(serviceName);
		log.info("namesrv lookup request,serviceName:{}", serviceName);
		int responseCode = serviceUri == null ? ResponseCode.SERVICE_NOT_FOUND : ResponseCode.OK;
		RemotingCommand response = RemotingCommand.newResponseCommand(responseCode, request.getCommandId(),
				SerializerSupport.serialize(serviceUri));
		log.info("namesrv lookup response,serviceURI:{}", serviceUri);
		ctx.channel().writeAndFlush(response);
	}

}
