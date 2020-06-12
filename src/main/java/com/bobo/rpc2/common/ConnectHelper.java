package com.bobo.rpc2.common;

import com.bobo.rpc2.transport.URI;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;

public class ConnectHelper {

	public static final int HEARTBEAT_INTERVAL = Integer.parseInt(System.getProperty("heartbeat.interval", "60000"));
	/**
	 * >=3*heartbeat_interval 留给客户端重试时间
	 */
	public static final int IDLE_TIMEOUT = Math.max(HEARTBEAT_INTERVAL * 3,
			Integer.parseInt(System.getProperty("idle.timeout", "180000")));

	public static final int CONNECT_TIMEOUT = Integer.parseInt(System.getProperty("connect.timeout.millis", "3000"));

	public static Channel connect(Bootstrap bootstrap, URI uri) {
		Channel channel = bootstrap.connect(uri.getHost(), uri.getPort()).awaitUninterruptibly().channel();
		if (channel == null || !channel.isActive()) {
			throw new IllegalStateException(String.format("connect to %s:%s failed", uri.getHost(), uri.getPort()));
		}
		return channel;
	}
}
