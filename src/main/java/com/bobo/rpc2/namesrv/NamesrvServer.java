package com.bobo.rpc2.namesrv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bobo.rpc2.common.ConnectHelper;
import com.bobo.rpc2.common.EventLoopGroupFactory;
import com.bobo.rpc2.common.LifeLine;
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
		bossGroup = EventLoopGroupFactory.newEventLoopGroup(1);
		workerGroup = EventLoopGroupFactory.newEventLoopGroup();

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childOption(ChannelOption.SO_REUSEADDR, true).childOption(ChannelOption.SO_KEEPALIVE, true)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) {
						ch.pipeline().addLast(new RemotingCommandCodec())
								.addLast(new IdleStateHandler(0, 0, ConnectHelper.IDLE_TIMEOUT))
								.addLast(ServerHandler.INSTANCE);
					}
				}).bind(PORT).addListener((ChannelFutureListener) future -> log.info("namesrv startup,port:{}", PORT));
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
