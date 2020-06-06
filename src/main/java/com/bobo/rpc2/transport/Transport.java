package com.bobo.rpc2.transport;

import java.io.Closeable;
import java.util.concurrent.CompletableFuture;

import io.netty.channel.Channel;

public class Transport implements Closeable {

	private Channel channel;

	public CompletableFuture<RemotingCommand> send(RemotingCommand command) {
		CompletableFuture<RemotingCommand> requestFuture = new CompletableFuture<>();
		RequestFutureContainer.addRequestFuture(command.getCommandId(), requestFuture);
		try {
			channel.writeAndFlush(command).addListener((future) -> {
				if (!future.isSuccess()) {
					close();
					requestFuture.completeExceptionally(future.cause());
					RequestFutureContainer.removeRequestFuture(command.getCommandId());
				}
			});
		} catch (Exception e) {
			requestFuture.completeExceptionally(e);
			RequestFutureContainer.removeRequestFuture(command.getCommandId());
		}
		return requestFuture;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Transport(Channel channel) {
		super();
		this.channel = channel;
	}

	@Override
	public void close() {
		if (channel != null) {
			channel.flush();
			channel.close();
		}
	}
}
