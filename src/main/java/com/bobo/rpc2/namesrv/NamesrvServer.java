package com.bobo.rpc2.namesrv;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bobo.rpc2.common.ConnectHelper;
import com.bobo.rpc2.common.EventLoopGroupFactory;
import com.bobo.rpc2.common.LifeLine;
import com.bobo.rpc2.common.ThreadFactoryFactory;
import com.bobo.rpc2.transport.codec.RemotingCommandCodec;
import com.bobo.rpc2.transport.handler.netty.ServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class NamesrvServer implements LifeLine {

	private static final Logger log = LoggerFactory.getLogger(NamesrvServer.class);
	private static final int PORT = Integer.parseInt(System.getProperty("namesrv.port", "8888"));

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	@Override
	public void start() {
		bossGroup = EventLoopGroupFactory.newEventLoopGroup(1, ThreadFactoryFactory.newThreadFactory("NameSrv_Boss"));
		workerGroup = EventLoopGroupFactory.newEventLoopGroup(ThreadFactoryFactory.newThreadFactory("NameSrv_Selector"));

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024).option(ChannelOption.SO_REUSEADDR, true)
				.childOption(ChannelOption.SO_KEEPALIVE, false).childOption(ChannelOption.TCP_NODELAY, true)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) {
						ch.pipeline().addLast(new RemotingCommandCodec())
								.addLast(new IdleStateHandler(0, 0, ConnectHelper.IDLE_TIMEOUT, TimeUnit.MILLISECONDS))
								.addLast(ServerHandler.INSTANCE);
					}
				}).bind(PORT).addListener((ChannelFutureListener) future -> {
					if (future.isSuccess()) {
						log.info("namesrv startup,port:" + PORT);
					} else {
						log.error("namesrv start failed", future.cause());
					}
				});

	}

	@Override
	public void stop() {
		if (bossGroup != null) {
			bossGroup.shutdownGracefully();
		}
		if (workerGroup != null) {
			workerGroup.shutdownGracefully();
		}
	}
}
