package com.bobo.rpc2.client;

@SuppressWarnings("rawtypes")
public class RpcConfig {

	private String serviceName;
	private Class serviceClass;
	/**
	 * 同步超时时间
	 */
	private long timeout;
	/**
	 * 执行方式 1-同步执行 2-异步执行
	 */
	private int invokeType;
	/**
	 * 异步执行时，指定回调
	 */
	private RpcCallback listener;

	private static final int INVOKE_AS_SYNC = 1;
	private static final int INVOKE_AS_ASYNC = 2;

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public int getInvokeType() {
		return invokeType;
	}

	public void setInvokeType(int invokeType) {
		this.invokeType = invokeType;
	}

	public RpcCallback getListener() {
		return listener;
	}

	public void setListener(RpcCallback listener) {
		this.listener = listener;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Class getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(Class serviceClass) {
		this.serviceClass = serviceClass;
	}

	public boolean willInvokeAsSync() {
		return INVOKE_AS_SYNC == invokeType;
	}

	public static RpcConfig newConfigAsSync(String serviceName, Class serviceClass, long timeout) {
		return new RpcConfig(serviceName, serviceClass, timeout, INVOKE_AS_SYNC, null);
	}

	public static RpcConfig newConfigAsAsync(String serviceName, Class serviceClass, long timeout,
			RpcCallback listener) {
		return new RpcConfig(serviceName, serviceClass, timeout, INVOKE_AS_ASYNC, listener);
	}

	public RpcConfig(String serviceName, Class serviceClass, long timeout, int invokeType, RpcCallback listener) {
		this.serviceName = serviceName;
		this.serviceClass = serviceClass;
		this.timeout = timeout;
		this.invokeType = invokeType;
		this.listener = listener;
	}

}
