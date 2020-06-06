package com.bobo.rpc2.transport.handler.biz;

import com.bobo.rpc2.transport.RemotingCommand;

import io.netty.channel.ChannelHandlerContext;

public interface RequestHandler {

	void handle(ChannelHandlerContext ctx, RemotingCommand request);
}
