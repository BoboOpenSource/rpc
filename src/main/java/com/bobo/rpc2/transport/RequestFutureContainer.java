package com.bobo.rpc2.transport;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RequestFutureContainer {
	private static Map<Integer, CompletableFuture<RemotingCommand>> pendingRequest = new ConcurrentHashMap<>();

	public static void addRequestFuture(Integer requestId, CompletableFuture<RemotingCommand> future) {
		pendingRequest.put(requestId, future);
	}

	public static void requestComplete(Integer requestId, RemotingCommand response) {
		CompletableFuture<RemotingCommand> future = pendingRequest.get(requestId);
		if (future != null) {
			future.complete(response);
		}
	}

	public static CompletableFuture<RemotingCommand> pullRequestFuture(Integer requestId) {
		CompletableFuture<RemotingCommand> future = pendingRequest.get(requestId);
		if (future != null) {
			removeRequestFuture(requestId);
		}
		return future;
	}

	public static void removeRequestFuture(Integer requestId) {
		pendingRequest.remove(requestId);
	}
}
