package com.bobo.rpc2.common;

import com.bobo.rpc2.transport.URI;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;

public class ConnectHelper {

	public static final int IDLE_TIMEOUT = Integer.parseInt(System.getProperty("idle.timeout.seconds", "60"));

	public static final int CONNECT_TIMEOUT = Integer.parseInt(System.getProperty("connect.timeout.millis", "3000"));

	public static Channel connect(Bootstrap bootstrap, URI uri) {
		Channel channel = bootstrap.connect(uri.getHost(), uri.getPort()).awaitUninterruptibly().channel();
		if (channel == null || !channel.isActive()) {
			throw new IllegalStateException(String.format("connect to %s:%s failed", uri.getHost(), uri.getPort()));
		}
		return channel;
	}
}
