package com.bobo.rpc2.namesrv;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bobo.rpc2.common.ConnectHelper;
import com.bobo.rpc2.common.Startup;
import com.bobo.rpc2.transport.codec.RemotingCommandCodec;
import com.bobo.rpc2.transport.handler.netty.ServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class NamesrvStartup implements Startup, Closeable {

	private static final Logger log = LoggerFactory.getLogger(NamesrvStartup.class);
	private static final int PORT = Integer.parseInt(System.getProperty("namesrv.port", "8888"));

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	@Override
	public void start() {
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup();

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
	public void close() throws IOException {
		if (bossGroup != null) {
			bossGroup.shutdownGracefully();
		}
		if (workerGroup != null) {
			workerGroup.shutdownGracefully();
		}
	}
}
