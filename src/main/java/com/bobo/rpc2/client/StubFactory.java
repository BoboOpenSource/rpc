package com.bobo.rpc2.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bobo.rpc2.common.exception.InvokeException;
import com.bobo.rpc2.transport.RemotingCommand;
import com.bobo.rpc2.transport.RequestCode;
import com.bobo.rpc2.transport.RpcRequest;
import com.bobo.rpc2.transport.Transport;
import com.bobo.rpc2.transport.seria.SerializerSupport;

public class StubFactory {

	private static final Logger log = LoggerFactory.getLogger(StubFactory.class);

	public static <T> T createStub(RpcConfig rpcConfig, Transport transport) {
		if (rpcConfig.willInvokeAsSync()) {
			return createStubAsSync(rpcConfig, transport);
		}
		return createStubAsAsync(rpcConfig, transport);
	}

	@SuppressWarnings("unchecked")
	private static <T> T createStubAsSync(RpcConfig rpcConfig, Transport transport) {
		return (T) Proxy.newProxyInstance(StubFactory.class.getClassLoader(),
				new Class[] { rpcConfig.getServiceClass() }, new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) {
						CompletableFuture<RemotingCommand> responseFuture = transport.send(buildRequestCommand(
								rpcConfig.getServiceName(), rpcConfig.getServiceClass(), method, args));
						try {
							RemotingCommand response = responseFuture.get(rpcConfig.getTimeout(),
									TimeUnit.MILLISECONDS);
							return SerializerSupport.deSerialize(response.getBody(), method.getReturnType());
						} catch (InterruptedException | ExecutionException | TimeoutException e) {
							log.error("rpc invoke failed", e);
							throw new InvokeException("rpc invoke failed", e);
						}
					}
				});
	}

	@SuppressWarnings("unchecked")
	private static <T> T createStubAsAsync(RpcConfig rpcConfig, Transport transport) {
		return (T) Proxy.newProxyInstance(StubFactory.class.getClassLoader(),
				new Class[] { rpcConfig.getServiceClass() }, new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						CompletableFuture<RemotingCommand> responseFuture = transport.send(buildRequestCommand(
								rpcConfig.getServiceName(), rpcConfig.getServiceClass(), method, args));
						responseFuture.thenAccept((future) -> {
							rpcConfig.getListener().invokeComplete(future);
						});
						return null;
					}
				});
	}

	private static <T> RemotingCommand buildRequestCommand(String serviceName, Class<T> serviceClass, Method method,
			Object[] args) {
		RpcRequest rpcRequest = new RpcRequest(serviceName, method.getName(), SerializerSupport.serialize(args[0]));
		return RemotingCommand.newRequestCommand(RequestCode.REMOTE_INVOKE, RemotingCommand.nextId(),
				SerializerSupport.serialize(rpcRequest));
	}

}
