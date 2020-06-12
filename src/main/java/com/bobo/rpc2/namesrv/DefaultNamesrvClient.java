package com.bobo.rpc2.namesrv;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.bobo.rpc2.common.ConnectHelper;
import com.bobo.rpc2.common.EventLoopGroupFactory;
import com.bobo.rpc2.common.ThreadFactoryFactory;
import com.bobo.rpc2.common.exception.LookupServiceException;
import com.bobo.rpc2.common.exception.RegisterServiceException;
import com.bobo.rpc2.server.ServiceURI;
import com.bobo.rpc2.transport.RemotingCommand;
import com.bobo.rpc2.transport.RequestCode;
import com.bobo.rpc2.transport.RequestFutureContainer;
import com.bobo.rpc2.transport.ResponseCode;
import com.bobo.rpc2.transport.Transport;
import com.bobo.rpc2.transport.URI;
import com.bobo.rpc2.transport.codec.RemotingCommandCodec;
import com.bobo.rpc2.transport.handler.netty.ClientHandler;
import com.bobo.rpc2.transport.seria.SerializerSupport;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class DefaultNamesrvClient implements NamesrvClient {

	private static final int WAITING_TIMEOUT = 3000;

	private EventLoopGroup eventLoopGroup;
	private Transport namesrvTransport;

	@Override
	public Channel connect(URI namesrvUri) {
		eventLoopGroup = EventLoopGroupFactory.newEventLoopGroup(ThreadFactoryFactory.newThreadFactory("NameSrvClient"));
		final Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ConnectHelper.CONNECT_TIMEOUT)
				.option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.SO_KEEPALIVE, false)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) {
						ch.pipeline().addLast(new RemotingCommandCodec())
								.addLast(new IdleStateHandler(0, 0, ConnectHelper.HEARTBEAT_INTERVAL,TimeUnit.MILLISECONDS))
								.addLast(new ClientHandler(bootstrap, namesrvUri, (newChannel) -> {
									if (newChannel != null) {
										namesrvTransport.setChannel(newChannel);
									}
								}));
					}
				});

		Channel channel = ConnectHelper.connect(bootstrap, namesrvUri);
		namesrvTransport = new Transport(channel);
		return channel;
	}

	@Override
	public void registerService(ServiceURI uri) {
		RemotingCommand registerRequest = RemotingCommand.newRequestCommand(RequestCode.REGISTER_SERVICE,
				RemotingCommand.nextId(), SerializerSupport.serialize(uri));
		CompletableFuture<RemotingCommand> response = namesrvTransport.send(registerRequest);
		try {
			RemotingCommand responseCommand = response.get(WAITING_TIMEOUT, TimeUnit.MILLISECONDS);
			if (responseCommand == null) {
				throw new RegisterServiceException("service register timeout");
			}
			if (ResponseCode.OK != responseCommand.getCode()) {
				throw new RegisterServiceException("service register failed,code=" + responseCommand.getCode());
			}
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			throw new RegisterServiceException("service register exception", e);
		} finally {
			RequestFutureContainer.removeRequestFuture(registerRequest.getCommandId());
		}
	}

	@Override
	public ServiceURI lookupService(String serviceName) {
		RemotingCommand lookupRequest = RemotingCommand.newRequestCommand(RequestCode.LOOKUP_SERVICE,
				RemotingCommand.nextId(), SerializerSupport.serialize(serviceName));
		CompletableFuture<RemotingCommand> future = namesrvTransport.send(lookupRequest);

		try {
			RemotingCommand responseCommand = future.get(WAITING_TIMEOUT, TimeUnit.MILLISECONDS);
			if (responseCommand == null) {
				throw new LookupServiceException("lookup service timeout");
			}
			if (ResponseCode.OK != responseCommand.getCode()) {
				throw new LookupServiceException("lookup service failed,code=" + responseCommand.getCode());
			}
			return SerializerSupport.deSerialize(responseCommand.getBody(), ServiceURI.class);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			throw new LookupServiceException("lookup service failed");
		} finally {
			RequestFutureContainer.removeRequestFuture(lookupRequest.getCommandId());
		}
	}

	@Override
	public void stop() {
		if (eventLoopGroup != null) {
			eventLoopGroup.shutdownGracefully();
		}
		if (namesrvTransport != null) {
			namesrvTransport.close();
		}
	}
}
