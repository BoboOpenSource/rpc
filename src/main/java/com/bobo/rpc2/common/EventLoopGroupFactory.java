package com.bobo.rpc2.common;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class EventLoopGroupFactory {

	public static EventLoopGroup newEventLoopGroup() {
		if (Epoll.isAvailable()) {
			return new EpollEventLoopGroup();
		} else {
			return new NioEventLoopGroup();
		}
	}

	public static EventLoopGroup newEventLoopGroup(int num) {
		if (Epoll.isAvailable()) {
			return new EpollEventLoopGroup(num);
		} else {
			return new NioEventLoopGroup(num);
		}
	}

	private EventLoopGroupFactory() {
	}
}
