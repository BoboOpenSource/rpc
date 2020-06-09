package com.bobo.rpc2.transport.handler.netty;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bobo.rpc2.common.CharsetHelper;
import com.bobo.rpc2.common.ConnectHelper;
import com.bobo.rpc2.common.ReconnectListener;
import com.bobo.rpc2.transport.RemotingCommand;
import com.bobo.rpc2.transport.RequestCode;
import com.bobo.rpc2.transport.RequestFutureContainer;
import com.bobo.rpc2.transport.URI;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

	private final Bootstrap bootstrap;
	private final URI uri;
	private ReconnectListener reconnectListener;

	public ClientHandler(Bootstrap bootstrap, URI uri) {
		this.bootstrap = bootstrap;
		this.uri = uri;
	}

	public ClientHandler(Bootstrap bootstrap, URI uri, ReconnectListener reconnectListener) {
		this.bootstrap = bootstrap;
		this.uri = uri;
		this.reconnectListener = reconnectListener;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		RemotingCommand command = (RemotingCommand) msg;
		int commandId = command.getCommandId();
		CompletableFuture<RemotingCommand> future = RequestFutureContainer.pullRequestFuture(commandId);
		if (future != null) {
			future.complete(command);
		}
	}

	private static final RemotingCommand PING_COMMAND = RemotingCommand.newRequestCommand(RequestCode.HEARTBEAT,
			RemotingCommand.nextId(), "ping".getBytes(CharsetHelper.UTF8));

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			ctx.channel().writeAndFlush(PING_COMMAND);
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = null;
		try {
			channel = ConnectHelper.connect(bootstrap, uri);
		} catch (IllegalStateException e) {
			log.info("reconnect to {}:{} failed", uri.getHost(), uri.getPort());
		} finally {
			if (reconnectListener != null) {
				reconnectListener.notify(channel);
			}
		}
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("client exceptionCaught", cause);
	}
}
