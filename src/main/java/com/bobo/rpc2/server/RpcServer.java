package com.bobo.rpc2.server;

import java.io.Closeable;

import com.bobo.rpc2.common.Startup;

public interface RpcServer extends Startup,Closeable{
	void registerService(String serviceName, Object o);
}
