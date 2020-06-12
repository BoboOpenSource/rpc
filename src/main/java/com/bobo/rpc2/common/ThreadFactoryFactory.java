package com.bobo.rpc2.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadFactoryFactory {

	public static ThreadFactory newThreadFactory(String threadName) {
		return new ThreadFactory() {
			AtomicInteger threadIndex = new AtomicInteger(0);

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, String.format("%s_%d", threadName, threadIndex.incrementAndGet()));
			}
		};
	}
}
