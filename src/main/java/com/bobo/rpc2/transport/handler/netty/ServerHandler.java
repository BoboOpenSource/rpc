package com.bobo.rpc2.transport.handler.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bobo.rpc2.transport.RemotingCommand;
import com.bobo.rpc2.transport.handler.biz.RequestHandlerSupport;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<RemotingCommand> {

	private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

	public static final ServerHandler INSTANCE = new ServerHandler();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RemotingCommand msg) throws Exception {
		log.info("receive command,code:{}", msg.getCode());
		RequestHandlerSupport.handle(ctx, msg);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("channelActive");
		super.channelActive(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			ctx.channel().close();
			log.info("channel disconnect:{}", ctx.channel());
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("server exceptionCaught", cause);
	}

	private ServerHandler() {
	}
}
