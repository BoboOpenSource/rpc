package com.bobo.rpc2.transport.handler.biz;

import com.bobo.rpc2.common.CharsetHelper;
import com.bobo.rpc2.transport.RemotingCommand;
import com.bobo.rpc2.transport.RequestCode;

import io.netty.channel.ChannelHandlerContext;

public class HeartbeatHandler implements RequestHandler {

	private static final RemotingCommand PONG_COMMAND = RemotingCommand.newResponseCommand(RequestCode.HEARTBEAT,
			RemotingCommand.nextId(), "pong".getBytes(CharsetHelper.UTF8));

	@Override
	public void handle(ChannelHandlerContext ctx, RemotingCommand request) {
		if ("ping".equals(new String(request.getBody(), CharsetHelper.UTF8))) {
			ctx.channel().writeAndFlush(PONG_COMMAND);
		}
	}

}
