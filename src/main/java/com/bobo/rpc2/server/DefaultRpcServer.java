package com.bobo.rpc2.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bobo.rpc2.common.ConnectHelper;
import com.bobo.rpc2.common.EventLoopGroupFactory;
import com.bobo.rpc2.common.InetAddressHelper;
import com.bobo.rpc2.namesrv.DefaultNamesrvClient;
import com.bobo.rpc2.transport.URI;
import com.bobo.rpc2.transport.URIType;
import com.bobo.rpc2.transport.codec.RemotingCommandCodec;
import com.bobo.rpc2.transport.handler.netty.ServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class DefaultRpcServer implements RpcServer {
	private static final Logger log = LoggerFactory.getLogger(DefaultRpcServer.class);

	private static final int RPC_SERVER_PORT = Integer.parseInt(System.getProperty("host", "6666"));
	private static final int BUSINESS_THREADS_NUM = Integer.parseInt(System.getProperty("business.threads.num", "30"));
	private static final String NAMESRV_HOST = System.getProperty("namesrv.host");
	private static final int NAMESRV_PORT = Integer.parseInt(System.getProperty("namesrv.port"));

	private final EventLoopGroup bossGroup;
	private final EventLoopGroup ioGroup;
	private final EventLoopGroup businessGroup;
	private final DefaultNamesrvClient namesrvClient;

	public DefaultRpcServer() {
		bossGroup = EventLoopGroupFactory.newEventLoopGroup(1);
		ioGroup = EventLoopGroupFactory.newEventLoopGroup();
		businessGroup = EventLoopGroupFactory.newEventLoopGroup(BUSINESS_THREADS_NUM);
		namesrvClient = new DefaultNamesrvClient();
	}

	@Override
	public void start() {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, ioGroup).channel(NioServerSocketChannel.class)
				.childOption(ChannelOption.SO_REUSEADDR, true).childOption(ChannelOption.SO_KEEPALIVE, true)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) {
						ch.pipeline().addLast(new RemotingCommandCodec())
								.addLast(new IdleStateHandler(0, 0, ConnectHelper.IDLE_TIMEOUT))
								.addLast(businessGroup, ServerHandler.INSTANCE);
					}
				}).bind(RPC_SERVER_PORT).addListener((future) -> {
					if (future.isSuccess()) {
						log.info("rpc server startup,port:{}", RPC_SERVER_PORT);
					}
				});

		namesrvClient.connect(URI.createURI(URIType.TYPE_TCP, NAMESRV_HOST, NAMESRV_PORT));
	}

	@Override
	public void registerService(String serviceName, Object o) {
		ServiceProviders.registerService(serviceName, o);
		ServiceURI uri = new ServiceURI(serviceName, URIType.TYPE_RPC, InetAddressHelper.getLocalIp(), RPC_SERVER_PORT);
		namesrvClient.registerService(uri);
	}

	@Override
	public void close() throws IOException {
		if (bossGroup != null) {
			bossGroup.shutdownGracefully();
		}
		if (ioGroup != null) {
			ioGroup.shutdownGracefully();
		}
		if (businessGroup != null) {
			businessGroup.shutdownGracefully();
		}
		if (namesrvClient != null) {
			namesrvClient.close();
		}
	}

}
