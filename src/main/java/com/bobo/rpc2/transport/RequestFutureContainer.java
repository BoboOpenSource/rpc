package com.bobo.rpc2.transport;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RequestFutureContainer {
	private static Map<Integer, CompletableFuture<RemotingCommand>> pendingRequests = new ConcurrentHashMap<>();

	public static void addRequestFuture(Integer requestId, CompletableFuture<RemotingCommand> future) {
		pendingRequests.put(requestId, future);
	}

	public static void requestComplete(Integer requestId, RemotingCommand response) {
		CompletableFuture<RemotingCommand> future = pendingRequests.get(requestId);
		if (future != null) {
			future.complete(response);
		}
	}

	public static CompletableFuture<RemotingCommand> pullRequestFuture(Integer requestId) {
		CompletableFuture<RemotingCommand> future = pendingRequests.get(requestId);
		if (future != null) {
			removeRequestFuture(requestId);
		}
		return future;
	}

	public static void removeRequestFuture(Integer requestId) {
		pendingRequests.remove(requestId);
	}
}
