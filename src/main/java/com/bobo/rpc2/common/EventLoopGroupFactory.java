package com.bobo.rpc2.common;

import java.util.concurrent.ThreadFactory;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class EventLoopGroupFactory {

	public static EventLoopGroup newEventLoopGroup() {
		return newEventLoopGroup(null);
	}

	public static EventLoopGroup newEventLoopGroup(ThreadFactory threadFactory) {
		if (Epoll.isAvailable()) {
			return new EpollEventLoopGroup(threadFactory);
		} else {
			return new NioEventLoopGroup(threadFactory);
		}
	}

	public static EventLoopGroup newEventLoopGroup(int num) {
		return newEventLoopGroup(num, null);
	}

	public static EventLoopGroup newEventLoopGroup(int num, ThreadFactory threadFactory) {
		if (Epoll.isAvailable()) {
			return new EpollEventLoopGroup(num, threadFactory);
		} else {
			return new NioEventLoopGroup(num, threadFactory);
		}
	}

	private EventLoopGroupFactory() {
	}
}
